package com.tele.crm.data.network.model.callLogs

import Lead

data class Data(
    val addedBy: AddedBy,
    val callLogs: List<CallLog>,
    val lead: Lead
)