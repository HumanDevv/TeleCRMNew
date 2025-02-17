package com.tele.crm.data.network.model.updateCampaign

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val createdById: String,
    val description: String,
    val leads: List<String>,
    val name: String,
    val updatedAt: String
)