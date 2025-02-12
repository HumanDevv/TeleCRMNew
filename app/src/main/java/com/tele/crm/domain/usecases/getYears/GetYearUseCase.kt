package com.tele.crm.domain.usecases.getYears

import com.tele.crm.data.network.model.getStreams.GetStreamResponse
import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.domain.usecases.NoInputUseCase


interface GetYearUseCase : NoInputUseCase<Result<GetYearResponse>>