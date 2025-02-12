package com.tele.crm.data.network.model.campaign

data class CampaignResponse(
    val success :Boolean,
    val campaign: Campaign,
    val message: String
)