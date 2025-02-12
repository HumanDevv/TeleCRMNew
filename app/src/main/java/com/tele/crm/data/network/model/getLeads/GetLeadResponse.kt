package com.tele.crm.data.network.model.getLeads

data class GetLeadResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)