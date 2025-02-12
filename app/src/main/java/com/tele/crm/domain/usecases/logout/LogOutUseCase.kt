package com.tele.crm.domain.usecases.logout


import com.tele.crm.data.network.model.logout.LogoutResponse
import com.tele.crm.domain.usecases.NoInputSuspendingUseCase

interface LogOutUseCase :
    NoInputSuspendingUseCase<Result<LogoutResponse>>