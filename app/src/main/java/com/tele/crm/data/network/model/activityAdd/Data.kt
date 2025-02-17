package com.tele.crm.data.network.model.activityAdd

data class Data(
    val __v: Int,
    val _id: String,
    val addedById: String,
    val addedByRole: String,
    val callLogs: List<CallLog>,
    val createdAt: String,
    val leadId: String,
    val updatedAt: String
)