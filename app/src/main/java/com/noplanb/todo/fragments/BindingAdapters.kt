package com.noplanb.todo.fragments

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.noplanb.todo.R

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
    }
}