package com.noplanb.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.todo.R
import com.noplanb.todo.data.viewmodel.ToDoViewModel
import com.noplanb.todo.databinding.FragmentListBinding
import com.noplanb.todo.fragments.SharedViewModel
import com.noplanb.todo.fragments.list.adapter.ListAdapter


class ListFragment : Fragment() {

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null  // auto generated for fragment_list.xml
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        // set up recycler view
        setupRecyclerView()

        // observe Live data
        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer{
            data-> adapter.setData(data)
            sharedViewModel.checkIfEmptyDatabase(data)
        })

        // set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        //swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteItem(itemToDelete)
                Toast.makeText(requireContext(),"Successfully deleted ${itemToDelete.title}", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoveAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoveAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {
                _, _ -> toDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully deleted all items." , Toast.LENGTH_SHORT).show();


        }
        builder.setNegativeButton("No") {_,_ ->}
        builder.setTitle("Delete All Items ")
        builder.setMessage("Are you sure you want to remove all items")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // very important - to avoid memory leak
    }
}