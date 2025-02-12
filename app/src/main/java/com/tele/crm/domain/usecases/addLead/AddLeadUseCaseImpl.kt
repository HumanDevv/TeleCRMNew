package com.tele.crm.domain.usecases.addLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.login.LoginUseCase

class AddLeadUseCaseImpl(
    val webRepository: CRMRepository
) : AddLeadUseCase {
     override suspend fun execute(input: AddLeadRequest): Result<AddLeadResponse> =
        webRepository.addLead(input)


}