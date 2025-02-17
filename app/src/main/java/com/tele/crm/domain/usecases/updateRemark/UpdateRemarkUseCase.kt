package com.tele.crm.domain.usecases.updateRemark

import com.tele.crm.data.network.model.updateRemark.UpdateRemarkRequest
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkResponse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface UpdateRemarkUseCase : SuspendingUseCase<UpdateRemarkRequest, Result<UpdateRemarkResponse>>