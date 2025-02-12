package com.tele.crm.domain.usecases.getLeads

import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.domain.usecases.NoInputUseCase

interface GetLeadUseCase : NoInputUseCase<Result<GetLeadResponse>>