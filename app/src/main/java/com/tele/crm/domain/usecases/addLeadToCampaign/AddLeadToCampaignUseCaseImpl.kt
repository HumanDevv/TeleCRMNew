package com.tele.crm.domain.usecases.addLeadToCampaign

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddLeadToCampaignResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.repository.CRMRepository

class AddLeadToCampaignUseCaseImpl(
    val webRepository: CRMRepository
) : AddLeadToCampaignUseCase {
     override suspend fun execute(input: AddToCampaignRequest): Result<AddLeadToCampaignResponse> =
        webRepository.addLeadToCampaign(input)


}