package com.tele.crm.data.network.model.updateStatus

data class UpdateStatusResopnse(
    val success: Boolean,
    val lead: Data,
    val message: String
)