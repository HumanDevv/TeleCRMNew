package com.tele.crm.domain.usecases.activityAdd

import com.tele.crm.data.network.model.activityAdd.ActivityAddResponse
import com.tele.crm.data.network.model.activityAdd.activtiyAddRequest
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.repository.CRMRepository

class AddActivityUseCaseImpl(
    val webRepository: CRMRepository
) : AddActivityUseCase {
     override suspend fun execute(input: activtiyAddRequest): Result<ActivityAddResponse> =
        webRepository.addActivity(input)


}