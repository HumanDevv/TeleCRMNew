package com.tele.crm.utils.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.R
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.utils.MetaItemBottomSheet

class MetaItemAdapter<T>(
    private val items: List<T>,
    private val onItemClick: (T) -> Unit,
    private val bottomSheetFragment: MetaItemBottomSheet<T>
) : RecyclerView.Adapter<MetaItemAdapter.GenericItemViewHolder<T>>() {

    class GenericItemViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvMetaTitle)  // Reusing the same TextView ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meta, parent, false)
        return GenericItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericItemViewHolder<T>, position: Int) {
        val item = items[position]

        holder.title.text = item.toString()

        holder.itemView.setOnClickListener {
            onItemClick(item)
            bottomSheetFragment.dismiss()
        }
    }

    override fun getItemCount(): Int = items.size
}