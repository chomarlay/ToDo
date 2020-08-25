package com.noplanb.todo.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
//import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.noplanb.todo.R
import com.noplanb.todo.data.models.Priority
import com.noplanb.todo.data.models.ToDoData

import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter(): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var dataList = emptyList<ToDoData>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view);
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.row_background.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.title_txt.text = dataList[position].title
        holder.itemView.description_txt.text = dataList[position].description
        val priority = dataList[position].priority
        when (priority){
            Priority.HIGH -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            Priority.MEDIUM -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
            Priority.LOW -> holder.itemView.priority_indicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }
    }

    fun setData(toDoData : List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }

}