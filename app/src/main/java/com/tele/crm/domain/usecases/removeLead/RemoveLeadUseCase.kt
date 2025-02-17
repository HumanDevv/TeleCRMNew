package com.tele.crm.domain.usecases.removeLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddLeadToCampaignResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.domain.usecases.SuspendingUseCase


interface RemoveLeadUseCase : SuspendingUseCase<AddToCampaignRequest, Result<AddLeadToCampaignResponse>>