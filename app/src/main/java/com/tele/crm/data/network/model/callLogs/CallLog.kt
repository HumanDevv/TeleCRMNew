package com.tele.crm.data.network.model.callLogs

data class CallLog(
    val callId: String,
    val duration: String,
    val outcome: String,
    val remarks: String,
    val status: String,
    val timestamp: String
)