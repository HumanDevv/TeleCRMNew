package com.tele.crm.domain.usecases.createCampaign

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaign.CampaignResponse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface CreateCampaignUseCase : SuspendingUseCase<CampaignRequest, Result<CampaignResponse>>