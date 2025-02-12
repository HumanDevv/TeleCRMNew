package com.tele.crm.domain.usecases.createCampaign

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaign.CampaignResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.login.LoginUseCase

class CreateCampaignUseCaseImpl(
    val webRepository: CRMRepository
) : CreateCampaignUseCase {
     override suspend fun execute(input: CampaignRequest): Result<CampaignResponse> =
        webRepository.createCampaign(input)


}