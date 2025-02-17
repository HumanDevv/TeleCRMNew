package com.tele.crm.domain.usecases.updateLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.repository.CRMRepository

class UpdateLeadUseCaseImpl(
    val webRepository: CRMRepository
) : UpdateLeadUseCase {
     override suspend fun execute(input: UpdateLeadUseCase.Input): Result<AddLeadResponse> {
         val result = webRepository.updateLead(input.request, input.requests)
         return result
     }
}