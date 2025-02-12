package com.tele.crm.domain.appevents

import kotlinx.coroutines.flow.SharedFlow

interface AppEventsHandler {

    val appEventsFlow: SharedFlow<AppEvents>
    suspend fun logout()
    suspend fun forceLogout()
    suspend fun login()
    suspend fun onBlock()
    suspend fun request502()
    suspend fun request413()
}
