package com.noplanb.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
//import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.databinding.RowLayoutBinding

class ListAdapter(): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    var dataList = emptyList<ToDoData>()

    class MyViewHolder(private val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData){
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }
        companion object {
            fun from (parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

    }

    fun setData(toDoData : List<ToDoData>) {

        val todoDiffUtil = ToDoDiffUtil(this.dataList, toDoData)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = toDoData
        todoDiffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()  // this is not good for performance,  use DiffUtil instead

    }

}