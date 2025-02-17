package com.tele.crm.data.network.model.updateRemark

data class UpdateRemarkRequest(
    val leadId: String,
    val callLogId: String,
    val remarks: String,
    val outcome: String
)
