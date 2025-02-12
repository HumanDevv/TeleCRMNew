package com.tele.crm.utils.extension

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class CallLogItem(
    val number: String,
    val duration: Int,
    val type: String,
    val timestamp: Long // Store timestamp
) {
    // Format duration into "X min Y sec"
    fun getFormattedDuration(): String {
        val minutes = duration / 60
        val seconds = duration % 60
        return if (minutes > 0) {
            "$minutes min $seconds sec"
        } else {
            "$seconds sec"
        }
    }

    // Format timestamp into "X days ago"
    fun getFormattedTimestamp(): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} min ago"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} hours ago"
            diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)} days ago"
            else -> SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp)) // Show full date if older
        }
    }
}
