package com.tele.crm.data.network.model.campaignDetails

import com.tele.crm.data.network.model.getLeads.Data

data class Data(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val createdById: String,
    val leads: List<Data>,
    val name: String,
    val updatedAt: String,
    val description: String,
)