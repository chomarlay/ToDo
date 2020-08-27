package com.noplanb.todo.fragments

import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.todo.R
import com.noplanb.todo.data.models.Priority
import com.noplanb.todo.data.models.ToDoData
import com.noplanb.todo.fragments.list.ListFragmentDirections

class BindingAdapters {
    companion object{

        @BindingAdapter("android:navigateToAddFragment")  // this id will be used in the floating button
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate:Boolean) {
            view.setOnClickListener{
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {view.setSelection(0)}
                Priority.MEDIUM -> {view.setSelection(1)}
                Priority.LOW -> {view.setSelection(2)}
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: Priority){
            when (priority) {
                Priority.HIGH -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
                    } else {
                        cardView.setBackgroundColor(Color.RED) // for my antique mobile :-)
                    }
                }
                Priority.MEDIUM -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
                    } else {
                        cardView.setBackgroundColor(Color.YELLOW) //for my antique mobile :-)
                    }
                }
                Priority.LOW -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
                    } else {
                        cardView.setBackgroundColor(Color.GREEN) //for my antique mobile :-)
                    }
                }

            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData) {
            view.setOnClickListener{
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }

    }
}