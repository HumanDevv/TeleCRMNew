package com.tele.crm.data.network.model.campaign

import Lead


data class Campaign(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: String,
    val createdById: String,
    val leads: List<Lead>,
    val name: String,
    val updatedAt: String
)