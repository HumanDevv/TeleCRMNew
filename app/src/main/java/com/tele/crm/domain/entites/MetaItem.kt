package com.tele.crm.domain.entites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class MetaItem(
    val id: String,
    var name: String
):Parcelable{
    override fun toString(): String {
        return name
    }
}
