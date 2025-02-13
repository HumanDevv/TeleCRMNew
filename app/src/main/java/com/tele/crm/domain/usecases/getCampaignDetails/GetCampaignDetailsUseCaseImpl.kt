package com.tele.crm.domain.usecases.getCampaignDetails

import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.callLogs.GetCallLogUsecase

class GetCampaignDetailsUseCaseImpl(
    val webRepository: CRMRepository,
) : GetCampaignDetailsUseCase {
    override suspend fun execute(campaignId: GetCampaignDetailsUseCase.Input): Result<CampaignDetailsResponse> {
        val result= webRepository.getCampaignDetails(campaignId.request)
        return result
    }

}