package com.tele.crm.data.network.model.getYear

data class GetYearResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)