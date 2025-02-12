package com.tele.crm.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationErrorDto(
    @Expose
    @SerializedName("status")
    val status: Int = 0,
    @Expose
    @SerializedName("message")
    val message: String = "",
    @Expose
    @SerializedName("data")
    val data: ErrorData = ErrorData()
)

@Serializable
data class ErrorData(
    @Expose
    @SerializedName("errors")
    val errors: List<Errors> = listOf())

@Serializable
data class Errors(
    @Expose
    @SerializedName("errField")
    val errField: String = "",
    @Expose
    @SerializedName("errText")
    val errText: String = "")