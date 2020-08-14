package com.atar.demo.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object CustomViewBinding {
    @JvmStatic
    @BindingAdapter(value = ["setAdapter"])
    fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        this.run {
            this.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["setOnScrollListener"])
    fun RecyclerView.addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        this.run {
            addOnScrollListener(listener)
        }
    }
}