package com.tele.crm.domain.appevents

sealed interface AppEvents {
    data object Logout : AppEvents
    data object ForceLogout : AppEvents
    data object Login : AppEvents
    data object OnBlock : AppEvents
    data object Request413 : AppEvents

    data object Request502 : AppEvents
}
