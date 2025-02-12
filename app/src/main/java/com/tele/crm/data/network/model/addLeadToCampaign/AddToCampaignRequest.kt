package com.tele.crm.data.network.model.addLeadToCampaign

data class AddToCampaignRequest (
    val campaignId: String,
    val leadIds: List<String>
)