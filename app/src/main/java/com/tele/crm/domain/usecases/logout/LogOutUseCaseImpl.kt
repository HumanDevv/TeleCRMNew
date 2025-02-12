package com.tele.crm.domain.usecases.logout


import com.tele.crm.data.network.model.logout.LogoutResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.logout.LogOutUseCase


class LogOutUseCaseImpl(
    private val webRepository: CRMRepository
) : LogOutUseCase {
    override suspend fun execute(): Result<LogoutResponse> {
        return webRepository.logout()
    }

}