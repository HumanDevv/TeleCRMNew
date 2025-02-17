package com.tele.crm.domain.usecases.updateLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.domain.usecases.SuspendingUseCase


interface UpdateLeadUseCase : SuspendingUseCase<UpdateLeadUseCase.Input, Result<AddLeadResponse>>{
    data class Input(val request: String,val requests: AddLeadRequest)

}