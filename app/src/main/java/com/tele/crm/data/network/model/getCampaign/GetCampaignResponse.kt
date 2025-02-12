package com.tele.crm.data.network.model.getCampaign

data class GetCampaignResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)