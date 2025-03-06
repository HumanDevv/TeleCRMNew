package com.tele.crm.presentation.lead

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.LeadsEntry
import com.tele.crm.data.network.model.getLeads.Data
import com.tele.crm.databinding.RowLeadsBinding
import com.tele.crm.utils.extension.getTimeAgo
import com.tele.crm.utils.extension.setDebouncedOnClickListener

class LeadsAdapter(private val listener: (Data) -> Unit) : RecyclerView.Adapter<LeadsAdapter.LeadViewHolder>() {

    private val leadList = mutableListOf<Data>()

    fun submitList(newList: List<Data>) {
        val diffCallback = LeadDiffCallback(leadList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        leadList.clear()
        leadList.addAll(newList)
        notifyDataSetChanged()  // Force refresh

    }


    class LeadViewHolder(
        private val binding: RowLeadsBinding,
        private val listener: (Data) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lead: Data) {
            binding.tvName.text = lead.name
            binding.tvPhoneNumber.text = lead.mobile
            binding.tvStatus.text = lead.status
            if (lead.stream != "") {
                binding.tvStream.visibility = View.VISIBLE

                binding.tvStream.text = lead.stream

            } else {
                binding.tvStream.visibility = View.GONE
            }
            if (lead.year != "") {
                binding.tvInterested.visibility = View.VISIBLE

                binding.tvInterested.text = lead.year

            } else {
                binding.tvInterested.visibility = View.GONE
            }
            binding.tvTime.text = getTimeAgo(lead.createdAt)

            // Handle item click
            itemView.setDebouncedOnClickListener {
                listener(lead)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = RowLeadsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.bind(leadList[position])
    }

    override fun getItemCount(): Int = leadList.size

    class LeadDiffCallback(
        private val oldList: List<Data>,
        private val newList: List<Data>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition]._id == newList[newItemPosition]._id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

}

