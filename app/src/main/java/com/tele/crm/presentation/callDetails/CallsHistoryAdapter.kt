package com.tele.crm.presentation.callDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.callLogs.CallLog
import com.tele.crm.databinding.RowCallHistoryBinding
import com.tele.crm.utils.extension.CallLogItem
import com.tele.crm.utils.extension.getTimeAgo

class CallsHistoryAdapter : RecyclerView.Adapter<CallsHistoryAdapter.CallViewHolder>() {

    private val callList = mutableListOf<CallLogItem?>()

    // Corrected: Now accepts a List<CallResponseItem?> (not a list of lists)
    fun submitList(list: List<CallLogItem>) {
        callList.clear()
        callList.addAll(list)
        notifyDataSetChanged()
    }

    // ViewHolder should bind individual CallResponseItem, not a list
    class CallViewHolder(private val binding: RowCallHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(call: CallLogItem?) {
            // Safely check for null
            call?.let {
                binding.tvTime.text = it.getFormattedTimestamp() ?: "Unknown"
                binding.tvStatus.text = it.type ?: "Unknown status"
                binding.tvDuration.text = it.getFormattedDuration()?: "Unknown duration"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val binding = RowCallHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bind(callList[position]) // Bind individual CallResponseItem
    }

    override fun getItemCount(): Int = callList.size
}
