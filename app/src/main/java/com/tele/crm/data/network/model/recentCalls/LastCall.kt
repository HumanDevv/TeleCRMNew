package com.tele.crm.data.network.model.recentCalls

data class LastCall(
    val callId: String,
    val duration: String,
    val outcome: String,
    val remarks: String,
    val status: String,
    val timestamp: String
)