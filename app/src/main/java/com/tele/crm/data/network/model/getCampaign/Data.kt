package com.tele.crm.data.network.model.getCampaign

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val createdById: String,
    val leads: List<Lead>,
    val name: String,
    val description: String?=null,
    val updatedAt: String
)