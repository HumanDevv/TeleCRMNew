package com.tele.crm.presentation.campaign

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.getCampaign.Data
import com.tele.crm.databinding.ItemCampaignBinding
import com.tele.crm.databinding.RowCampaignBinding
import com.tele.crm.utils.extension.setDebouncedOnClickListener

class CampaignAdapter(private val listener: (Data) -> Unit) : RecyclerView.Adapter<CampaignAdapter.LeadViewHolder>() {

    private val callList = mutableListOf<Data>()

    fun submitList(list: List<Data>) {
        callList.clear()
        callList.addAll(list)
        notifyDataSetChanged()
    }

    class LeadViewHolder(private val binding: ItemCampaignBinding,
                         private val listener: (Data) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(campaign: Data) {
            binding.tvName.text = campaign.name
            binding.tvCount.text =campaign.leads.size.toString()
            if (!campaign.description.isNullOrEmpty()) {
                binding.tvDescription.text = campaign.description
                binding.layoutDescription.visibility = View.VISIBLE
            } else {
                binding.layoutDescription.visibility = View.GONE  // Hide layout if description is missing or empty
}
                itemView.setDebouncedOnClickListener {
                listener.invoke(campaign)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = ItemCampaignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.bind(callList[position])
    }

    override fun getItemCount(): Int = callList.size
}
