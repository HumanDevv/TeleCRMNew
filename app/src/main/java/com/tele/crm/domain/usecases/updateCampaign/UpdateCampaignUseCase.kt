package com.tele.crm.domain.usecases.updateCampaign

import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.updateCampaign.UpdateCampaignResponse
import com.tele.crm.domain.usecases.SuspendingUseCase

interface UpdateCampaignUseCase : SuspendingUseCase<UpdateCampaignUseCase.Input, Result<UpdateCampaignResponse>> {
    data class Input(val request: String,val requests:CampaignRequest
    )

}
