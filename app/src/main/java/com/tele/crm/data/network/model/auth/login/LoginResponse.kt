package com.tele.crm.data.network.model.auth.login

data class LoginResponse(
    val success :Boolean,
    val `data`: Data,
    val message: String
)