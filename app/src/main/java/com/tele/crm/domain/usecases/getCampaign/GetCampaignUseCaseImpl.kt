package com.tele.crm.domain.usecases.getCampaign

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.profile.ProfileResponse
import com.tele.crm.data.network.repository.CRMRepository

class GetCampaignUseCaseImpl(
    val webRepository: CRMRepository,
) : GetCampaignUseCase {


    override suspend fun execute(): Result<GetCampaignResponse> {
        val result= webRepository.getCampaign()
        return result
    }

}