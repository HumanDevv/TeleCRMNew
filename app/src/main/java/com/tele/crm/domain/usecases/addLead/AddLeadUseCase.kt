package com.tele.crm.domain.usecases.addLead

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface AddLeadUseCase : SuspendingUseCase<AddLeadRequest, Result<AddLeadResponse>>