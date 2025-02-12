package com.tele.crm.domain.usecases.getLeads

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.profile.ProfileResponse
import com.tele.crm.data.network.repository.CRMRepository

class GetLeadUseCaseImpl(
    val webRepository: CRMRepository,
) : GetLeadUseCase {


    override suspend fun execute(): Result<GetLeadResponse> {
        val result= webRepository.getLeads()
        return result
    }

}