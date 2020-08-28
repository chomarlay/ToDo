package com.noplanb.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noplanb.todo.R
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.data.viewmodel.ToDoViewModel
import com.noplanb.todo.databinding.FragmentListBinding
import com.noplanb.todo.fragments.SharedViewModel
import com.noplanb.todo.fragments.list.adapter.ListAdapter
import com.noplanb.todo.utils.hideKeyboard
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

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

        // hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = SlideInUpAnimator().apply { addDuration = 300 }

        //swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object: SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteItem(deletedItem)
//                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedItem(viewHolder.itemView,deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreDeletedItem(view: View, deletedItem: ToDoData) {
        val snackbar = Snackbar.make(view, "Deleted ${deletedItem.title}", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            toDoViewModel.insertData(deletedItem)
//            adapter.notifyItemChanged(position) //still works without it
        }
        snackbar.show()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoveAll()
            R.id.menu_priority_high -> sortByHighPriority()
            R.id.menu_priority_low -> sortByLowPriority()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortByHighPriority() {
        toDoViewModel.sortByHighPriority.observe(this, Observer{
            adapter.setData(it)
        })
    }

    private fun sortByLowPriority() {
        toDoViewModel.sortByLowPriority.observe(this, Observer{
            adapter.setData(it)
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }
    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%${query}%"

        toDoViewModel.searchDatabase(searchQuery).observe(this, Observer{
//                data-> adapter.setData(data)  / this one work too.. don't quite understand
            list->list?.let{adapter.setData(it)}
        })

    }

    private fun confirmRemoveAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {
                _, _ -> toDoViewModel.deleteAll()
            Toast.makeText(requireContext(), "Successfully deleted all items." , Toast.LENGTH_SHORT).show()


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