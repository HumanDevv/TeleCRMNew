package com.tele.crm.data.network.model

data class LeadsEntry(
    val name: String,
    val phoneNumber: String,
    val status: String,
    val duration: String,
    val timeAgo: String,
    val isStarred: Boolean
)
