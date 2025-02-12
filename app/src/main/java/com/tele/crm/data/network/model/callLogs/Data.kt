package com.tele.crm.data.network.model.callLogs

data class Data(
    val addedBy: AddedBy,
    val callLogs: List<CallLog>,
    val lead: Lead
)