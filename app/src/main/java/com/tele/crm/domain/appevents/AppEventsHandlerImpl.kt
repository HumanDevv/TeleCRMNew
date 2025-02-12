package com.tele.crm.domain.appevents

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppEventsHandlerImpl : AppEventsHandler {

    private val _appEventsFlow = MutableSharedFlow<AppEvents>()

    override val appEventsFlow: SharedFlow<AppEvents>
        get() = _appEventsFlow.asSharedFlow()

    override suspend fun logout() {
        _appEventsFlow.emit(AppEvents.Logout)
    }

    override suspend fun forceLogout() {
        _appEventsFlow.emit(AppEvents.ForceLogout)
    }

    override suspend fun login() {
        _appEventsFlow.emit(AppEvents.Login)
    }

    override suspend fun onBlock() {
        _appEventsFlow.emit(AppEvents.OnBlock)
    }


    override suspend fun request502() {
        _appEventsFlow.emit(AppEvents.Request502)
    }
    override suspend fun request413() {
        _appEventsFlow.emit(AppEvents.Request413)
    }


}
