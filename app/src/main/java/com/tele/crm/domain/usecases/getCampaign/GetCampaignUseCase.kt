package com.tele.crm.domain.usecases.getCampaign

import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.domain.usecases.NoInputUseCase

interface GetCampaignUseCase : NoInputUseCase<Result<GetCampaignResponse>>