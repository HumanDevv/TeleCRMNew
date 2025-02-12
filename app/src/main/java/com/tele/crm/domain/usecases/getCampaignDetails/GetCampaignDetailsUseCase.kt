package com.tele.crm.domain.usecases.getCampaignDetails

import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.domain.usecases.NoInputUseCase
import com.tele.crm.domain.usecases.SuspendingUseCase

interface GetCampaignDetailsUseCase :
    SuspendingUseCase<GetCampaignDetailsUseCase.Input, Result<CampaignDetailsResponse>> {
    data class Input(val request: String)

}