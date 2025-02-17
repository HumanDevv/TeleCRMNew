package com.tele.crm.presentation.callDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tele.crm.data.network.model.callLogs.CallLog
import com.tele.crm.databinding.RowCallHistoryBinding
import com.tele.crm.utils.extension.CallLogItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class CallsHistoryAdapter(
    private val onRemarkSubmitted: (String, String,String) -> Unit // Lambda to send remark to fragment

) : RecyclerView.Adapter<CallsHistoryAdapter.CallViewHolder>() {

    private val callList = mutableListOf<CallLog?>()

    // Corrected: Now accepts a List<CallResponseItem?> (not a list of lists)
    fun submitList(list: List<CallLog>) {
        callList.clear()
        callList.addAll(list)
        notifyDataSetChanged()
    }

    // ViewHolder should bind individual CallResponseItem, not a list
    class CallViewHolder(private val binding: RowCallHistoryBinding,
                         private val onRemarkSubmitted: (String, String,String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(call: CallLog?) {
            // Safely check for null
            call?.let {
                binding.tvTime.text = getFormattedTimestamp(it.timestamp) ?: "Unknown"
                binding.tvStatus.text = it.status ?: "Unknown status"
                binding.tvDuration.text = it.duration ?: "Unknown duration"
                binding.etRemark.setText( it.remarks)
                    binding.etRemark.visibility = View.GONE
                    binding.btnSave.visibility = View.GONE


                itemView.setOnClickListener {
                    if (binding.etRemark.visibility == View.VISIBLE) {

                            binding.etRemark.visibility = View.GONE
                            binding.btnSave.visibility = View.GONE

                    } else {
                        binding.etRemark.visibility = View.VISIBLE
                        binding.btnSave.visibility = View.VISIBLE
                    }
                }

                binding.btnSave.setOnClickListener {
                    val remarkText = binding.etRemark.text.toString().trim()
                    if (remarkText.isNotEmpty()) {
                        onRemarkSubmitted(
                            remarkText,
                            call.callId,
                            call.outcome
                        )
                        binding.etRemark.visibility = View.GONE
                        binding.btnSave.visibility = View.GONE
                    }
                }
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val binding = RowCallHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CallViewHolder(binding,onRemarkSubmitted)
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bind(callList[position]) // Bind individual CallResponseItem
    }

    override fun getItemCount(): Int = callList.size


}


fun getFormattedTimestamp(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC") // Ensure it parses UTC time correctly
        }
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val date = inputFormat.parse(timestamp) // Convert string to Date
        date?.let { outputFormat.format(it) } ?: "Invalid date"
    } catch (e: Exception) {
        "Invalid date"
    }
}
