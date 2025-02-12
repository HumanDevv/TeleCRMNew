package com.tele.crm.data.datastore


import kotlinx.serialization.Serializable

@Serializable
data class AppDataStoreDto(
    val isUserLoggedIn: Boolean = false,
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val mobile: String = "",
    val email: String = "",
    val token: String = "",

)
