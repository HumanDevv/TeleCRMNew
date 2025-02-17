package com.tele.crm.domain.usecases.updateCampaign

import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.updateCampaign.UpdateCampaignResponse
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusResopnse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.getCampaignDetails.GetCampaignDetailsUseCase

class UpdateCampaignUseCaseImpl(
    val webRepository: CRMRepository,
) : UpdateCampaignUseCase {
    override suspend fun execute(campaignId: UpdateCampaignUseCase.Input): Result<UpdateCampaignResponse> {
        val result = webRepository.updateCampaign(campaignId.request,campaignId.requests)
        return result
    }
}
