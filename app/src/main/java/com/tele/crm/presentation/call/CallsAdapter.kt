package com.tele.crm.presentation.call

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.recentCalls.Data
import com.tele.crm.databinding.RowCallsBinding
import com.tele.crm.utils.extension.getTimeAgo
import com.tele.crm.utils.extension.setDebouncedOnClickListener

class CallsAdapter(private val listener: (Data) -> Unit) : RecyclerView.Adapter<CallsAdapter.CallViewHolder>() {

    private val callList = mutableListOf<Data?>()

    // Corrected: Now accepts a List<CallResponseItem?> (not a list of lists)
    fun submitList(list: List<Data?>) {
        callList.clear()
        callList.addAll(list)
        notifyDataSetChanged()
    }

    // ViewHolder should bind individual CallResponseItem, not a list
    class CallViewHolder(private val binding: RowCallsBinding,
                         private val listener: (Data) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(call: Data?) {
            // Safely check for null
            call?.let {
                binding.tvName.text = it.name ?: "Unknown"
                binding.tvPhoneNumber.text = it.mobile ?: "No number"
                binding.tvStatus.text = it.lastCall?.status ?: "Unknown status"
                binding.tvDuration.text = it.lastCall?.remarks ?: "Unknown Remark"
                binding.tvTimestatus.text = it.lastCall?.duration ?: "Unknown duration"
                binding.tvTime.text = getTimeAgo(it.lastCall.timestamp)
                itemView.setDebouncedOnClickListener {
                    listener(call)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val binding = RowCallsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bind(callList[position]) // Bind individual CallResponseItem
    }

    override fun getItemCount(): Int = callList.size
}
