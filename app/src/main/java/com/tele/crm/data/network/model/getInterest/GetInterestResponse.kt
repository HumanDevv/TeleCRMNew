package com.tele.crm.data.network.model.getInterest

data class GetInterestResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)