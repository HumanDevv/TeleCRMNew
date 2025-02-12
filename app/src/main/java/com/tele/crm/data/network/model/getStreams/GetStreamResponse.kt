package com.tele.crm.data.network.model.getStreams

data class GetStreamResponse(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)