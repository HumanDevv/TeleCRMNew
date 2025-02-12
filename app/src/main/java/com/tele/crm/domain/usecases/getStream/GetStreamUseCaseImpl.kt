package com.tele.crm.domain.usecases.getStream

import com.tele.crm.data.network.model.getStreams.GetStreamResponse
import com.tele.crm.data.network.repository.CRMRepository

class GetStreamUseCaseImpl(
    val webRepository: CRMRepository
) : GetStreamUseCase {


    override suspend fun execute(): Result<GetStreamResponse> {
        return webRepository.getStream()
    }


}