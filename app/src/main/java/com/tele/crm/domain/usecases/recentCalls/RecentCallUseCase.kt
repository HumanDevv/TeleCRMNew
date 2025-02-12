package com.tele.crm.domain.usecases.recentCalls

import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.recentCalls.CallResponse
import com.tele.crm.domain.usecases.NoInputUseCase


interface RecentCallUseCase : NoInputUseCase<Result<CallResponse>>