package com.tele.crm.domain.usecases.getInterest

import com.tele.crm.data.network.model.getInterest.GetInterestResponse
import com.tele.crm.data.network.repository.CRMRepository

class GetInterestUseCaseImpl(
    val webRepository: CRMRepository
) : GetInterestUseCase {


    override suspend fun execute(): Result<GetInterestResponse> {
        return webRepository.getInterest()
    }


}