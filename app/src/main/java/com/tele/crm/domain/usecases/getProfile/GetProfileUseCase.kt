package com.tele.crm.domain.usecases.getProfile

import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.profile.ProfileResponse
import com.tele.crm.domain.usecases.NoInputUseCase


interface GetProfileUseCase : NoInputUseCase<Result<ProfileResponse>>