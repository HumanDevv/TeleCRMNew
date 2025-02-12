package com.tele.crm.data.network.model.profile

data class ProfileResponse(
    val `data`: Data,
    val message: String,
    val success: Boolean
)