package com.tele.crm.domain.usecases.getStream

import com.tele.crm.data.network.model.getStreams.GetStreamResponse
import com.tele.crm.domain.usecases.NoInputUseCase


interface GetStreamUseCase : NoInputUseCase<Result<GetStreamResponse>>