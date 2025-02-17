package com.tele.crm.domain.usecases.updateRemark

import com.tele.crm.data.network.model.updateRemark.UpdateRemarkRequest
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkResponse
import com.tele.crm.data.network.repository.CRMRepository

class UpdateRemarkUseCaseImpl(
    val webRepository: CRMRepository
) : UpdateRemarkUseCase {
     override suspend fun execute(input: UpdateRemarkRequest): Result<UpdateRemarkResponse> =
        webRepository.updateRemark(input)


}