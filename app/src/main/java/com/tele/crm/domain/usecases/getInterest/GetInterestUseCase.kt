package com.tele.crm.domain.usecases.getInterest

import com.tele.crm.data.network.model.getInterest.GetInterestResponse
import com.tele.crm.domain.usecases.NoInputUseCase


interface GetInterestUseCase : NoInputUseCase<Result<GetInterestResponse>>