package com.tele.crm.data.network.model.addLeadToCampaign

data class AddLeadToCampaignResponse(
    val success :Boolean,
    val campaign: Campaign,
    val message: String
)