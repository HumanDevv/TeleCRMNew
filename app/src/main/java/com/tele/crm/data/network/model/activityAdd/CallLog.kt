package com.tele.crm.data.network.model.activityAdd

data class CallLog(
    val _id: String,
    val duration: Int,
    val outcome: String,
    val remarks: String,
    val status: String,
    val timestamp: String
)