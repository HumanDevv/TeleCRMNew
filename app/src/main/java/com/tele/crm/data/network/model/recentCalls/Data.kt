package com.tele.crm.data.network.model.recentCalls

data class Data(
    val campaigns: List<Any>,
    val lastCall: LastCall,
    val leadId: String,
    val mobile: String,
    val name: String
)