package com.atar.demo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atar.demo.R
import com.atar.demo.model.BaseItem
import com.atar.demo.model.ListItem
import com.atar.demo.model.LoadMoreItem
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.load_more_item.view.*


class MainListAdapter(
    private val context: Context,
    private val listItems: ArrayList<BaseItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoadingAdded = false

    companion object {
        const val ITEM = 1
        const val LOAD_MORE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (viewType == ITEM)
            ItemViewHolder(inflater.inflate(R.layout.list_item, parent, false))
        else
            ItemViewHolder(inflater.inflate(R.layout.load_more_item, parent, false))
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
                val item = listItems[position] as ListItem
                holder.item.name.text = "Name: ${item.name}"
                holder.item.desc.text = "Description: ${item.description}"
                holder.item.open_issue_count.text = "Open Issues: ${item.open_issues_count}"
                holder.item.licence.text = "Licence: ${item.license?.name}"
                holder.item.permissions.text =
                    "Permission: Admin[${item.permissions.admin}], Pull[${item.permissions.pull}], Push[${item.permissions.push}]"
            }
        } else {
            if (holder is LoadMoreViewHolder) {
                holder.item.load_more.visibility = View.VISIBLE
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

internal class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item: View = view
}

internal class LoadMoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val item: View = view
}
