package com.noplanb.todo.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.noplanb.todo.R
import com.noplanb.todo.data.models.Priority
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.data.viewmodel.ToDoViewModel
import com.noplanb.todo.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)
        setHasOptionsMenu(true)
        view.priorities_spinner.onItemSelectedListener = sharedViewModel.listener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // save 'Tick' menu button is clicked
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = title_et.text.toString()
        val mDescription = description_et.text.toString()
        val mPriority = priorities_spinner.selectedItem.toString()

        val validData = sharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if (validData) {
            val newData = ToDoData(0, mTitle, sharedViewModel.parsePriority(mPriority), mDescription )
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Successfully added ToDo data", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(),"Please enter Title and description", Toast.LENGTH_SHORT).show()
        }

    }


}