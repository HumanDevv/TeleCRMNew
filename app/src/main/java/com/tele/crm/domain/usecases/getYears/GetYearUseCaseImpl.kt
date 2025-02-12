package com.tele.crm.domain.usecases.getYears

import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.getProfile.GetProfileUseCase

class GetYearUseCaseImpl(
    val webRepository: CRMRepository
) : GetYearUseCase {


    override suspend fun execute(): Result<GetYearResponse> {
        return webRepository.getYear()
    }

}