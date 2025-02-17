package com.tele.crm.data.network.model.callLogs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Campaign(
    val id: String,
    val name: String
):Parcelable