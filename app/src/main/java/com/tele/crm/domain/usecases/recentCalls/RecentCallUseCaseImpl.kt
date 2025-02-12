package com.tele.crm.domain.usecases.recentCalls

import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.recentCalls.CallResponse
import com.tele.crm.data.network.repository.CRMRepository

class RecentCallUseCaseImpl(
    val webRepository: CRMRepository
) : RecentCallUseCase {


    override suspend fun execute(): Result<CallResponse> {
        return webRepository.recentCalls()
    }

}