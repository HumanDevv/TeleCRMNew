package com.tele.crm.data.network.model.updateRemark

data class Data(
    val callLogId: String,
    val leadId: String,
    val outcome: String,
    val remarks: String
)