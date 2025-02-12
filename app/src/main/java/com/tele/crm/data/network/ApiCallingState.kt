package com.tele.crm.data.network

sealed class ApiCallingState<out T> {
    data object Idle : ApiCallingState<Nothing>()
    data class Loading(val isLoading: Boolean = false) : ApiCallingState<Nothing>()
    data class Success<T>(var value: T) : ApiCallingState<T>()
    data class Fail<T>(var value: T) : ApiCallingState<T>()
    sealed class Failure : ApiCallingState<Nothing>() {
        abstract val isAtLeastOnePageLoaded: Boolean
        abstract val throwable: Throwable

        data class Unknown(
            override val isAtLeastOnePageLoaded: Boolean = false,
            override val throwable: Throwable,
        ) : Failure()
    }

    operator fun invoke() = (this as? Success)?.value
}
