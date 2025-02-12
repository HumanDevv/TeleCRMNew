package com.tele.crm.data.network.model.addLead

data class AddLeadResponse(
    val success :Boolean,
    val lead: Lead,
    val message: String
)
