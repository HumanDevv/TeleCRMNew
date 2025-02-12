package com.tele.crm.data.network

sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure<T>(val message: String, val throwable: Throwable? = null) : ApiResponse<T>()
    class Loading<T> : ApiResponse<T>()
}
