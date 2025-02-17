package com.tele.crm.utils.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.R
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.utils.CampaignBottomSheet

class CampaignItemAdapter(
    private val items: List<MetaItem>,
    private val onItemClick: (MetaItem) -> Unit,
    val campaignList: List<String>,
    private val bottomSheetFragment: CampaignBottomSheet
) : RecyclerView.Adapter<CampaignItemAdapter.GenericItemViewHolder<MetaItem>>() {


    class GenericItemViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvMetaTitle)
        val check: ImageView = view.findViewById(R.id.ivSelectIcon)
        val views: View = view.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemViewHolder<MetaItem> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meta, parent, false)
        return GenericItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericItemViewHolder<MetaItem>, position: Int) {
        val item = items[position]

        // Check if the campaignList has enough elements
        val campaign = if (position < campaignList.size) campaignList[position] else null

        holder.title.text = item.toString()

        if (campaignList.contains(item.id)) {
            holder.check.visibility = View.VISIBLE
        } else {
            holder.check.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
            bottomSheetFragment.dismiss()
        }
    }

    override fun getItemCount(): Int = items.size
}