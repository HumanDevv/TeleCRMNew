package com.tele.crm.data.network.model.campaignDetails

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val createdById: String,
    val leads: List<Lead>,
    val name: String,
    val updatedAt: String
)