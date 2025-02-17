package com.tele.crm.domain.usecases.removeLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddLeadToCampaignResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.addLead.AddLeadUseCase

class RemoveLeadUseCaseImpl(
    val webRepository: CRMRepository
) : RemoveLeadUseCase {
     override suspend fun execute(input: AddToCampaignRequest): Result<AddLeadToCampaignResponse> =
        webRepository.removeLeadToCampaign(input)


}