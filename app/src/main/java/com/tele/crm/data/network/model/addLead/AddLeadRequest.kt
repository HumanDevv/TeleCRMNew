package com.tele.crm.data.network.model.addLead

data class AddLeadRequest(
    val name: String,
    val mobile: String,
    val emailId: String,
    val alternateMobile: String? = null,
    val stream: String? = null,
    val year: String? = null,
    val address: String? = null,
    val interestedIn: String? = null,
    val collegeName: String? = null,
    val counsellingDate: String? = null,
    val status: String? = null
)
