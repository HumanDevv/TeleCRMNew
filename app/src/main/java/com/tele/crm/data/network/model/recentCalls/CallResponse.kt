package com.tele.crm.data.network.model.recentCalls

import com.google.gson.annotations.SerializedName

data class CallResponse  ( val `data`: List<Data>,
val message: String,
val success: Boolean)