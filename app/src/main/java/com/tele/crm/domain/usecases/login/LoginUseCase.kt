package com.tele.crm.domain.usecases.login

import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface LoginUseCase : SuspendingUseCase<LoginRequest, Result<LoginResponse>>