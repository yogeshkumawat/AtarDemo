package com.atar.demo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atar.demo.databinding.ListItemBinding
import com.atar.demo.databinding.LoadMoreItemBinding
import com.atar.demo.model.BaseItem
import com.atar.demo.model.ListItem
import com.atar.demo.model.LoadMoreItem


class MainListAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingAdded = false

    var listItems: ArrayList<BaseItem> = ArrayList()

    companion object {
        const val ITEM = 1
        const val LOAD_MORE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == ITEM)
            ItemViewHolder(ListItemBinding.inflate(inflater))
        else
            LoadMoreViewHolder(LoadMoreItemBinding.inflate(inflater))
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == listItems.size - 1 && isLoadingAdded) LOAD_MORE else ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM) {
            if (holder is ItemViewHolder) {
                holder.binding.listItem = listItems[position] as ListItem
                holder.binding.executePendingBindings()
            }
        } else {
            if (holder is LoadMoreViewHolder) {
                holder.binding.loadMore.visibility = View.VISIBLE
                holder.binding.executePendingBindings()
            }
        }

    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        listItems.add(LoadMoreItem())
        notifyItemInserted(listItems.size - 1)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = listItems.size - 1
        val result = listItems[position]
        listItems.remove(result)
        notifyItemRemoved(position)
    }

    fun addListItems(data: java.util.ArrayList<ListItem>) {
        for (listItem in data)
            addItem(listItem)
    }

    private fun addItem(item: ListItem) {
        listItems.add(item)
        notifyItemInserted(listItems.size - 1)
    }
}

internal class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

internal class LoadMoreViewHolder(val binding: LoadMoreItemBinding) :
    RecyclerView.ViewHolder(binding.root)
