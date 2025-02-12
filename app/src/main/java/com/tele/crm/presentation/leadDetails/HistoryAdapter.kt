package com.tele.crm.presentation.leadDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.leadDetails.History
import com.tele.crm.databinding.RowHistoryBinding
import com.tele.crm.databinding.RowLeadsBinding
import com.tele.crm.utils.extension.setDebouncedOnClickListener

class HistoryAdapter(private val listener: (History) -> Unit) : RecyclerView.Adapter<HistoryAdapter.LeadViewHolder>() {

    private val leadList = mutableListOf<History>()

    fun submitList(list: List<History>) {
        leadList.clear()
        leadList.addAll(list)
        notifyDataSetChanged()
    }

    class LeadViewHolder(
        private val binding: RowHistoryBinding,
        private val listener: (History) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.tvCallDuration.text = history.duration
            binding.tvCallTime.text = history.time
            binding.tvCallStatus.text = history.status

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = RowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        holder.bind(leadList[position])
    }

    override fun getItemCount(): Int = leadList.size
}
