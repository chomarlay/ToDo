package com.noplanb.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.noplanb.todo.R
import com.noplanb.todo.data.models.Priority
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.data.viewmodel.ToDoViewModel
import com.noplanb.todo.fragments.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import kotlinx.android.synthetic.main.fragment_update.view.current_title_et
import kotlinx.android.synthetic.main.row_layout.view.*

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        setHasOptionsMenu(true)

        // set value from the args
        view.current_title_et.setText(args.currentItem.title)
        view.current_description_et.setText(args.currentItem.description)
        view.current_priorities_spinner.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        // set the color of the priority
        view.current_priorities_spinner.onItemSelectedListener = sharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateData()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun updateData() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val priority = current_priorities_spinner.selectedItem.toString()
        val validData = sharedViewModel.verifyDataFromUser(title, description)
        if (validData) {
            val updatedData = ToDoData(args.currentItem.id, title, sharedViewModel.parsePriority(priority), description)
            viewModel.updateData(updatedData)
            Toast.makeText(requireContext(), "Successfully updated data." , Toast.LENGTH_SHORT).show();
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please enter all data fields." , Toast.LENGTH_SHORT).show();
        }

    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {
                _, _ -> viewModel.deleteItem(args.currentItem)
            Toast.makeText(requireContext(), "Successfully deleted '${args.currentItem.title}'." , Toast.LENGTH_SHORT).show();
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_,_ ->}
        builder.setTitle("Delete '${args.currentItem.title}' ")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'")
        builder.create().show()

    }
}