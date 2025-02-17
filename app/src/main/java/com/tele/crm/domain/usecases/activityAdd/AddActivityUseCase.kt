package com.tele.crm.domain.usecases.activityAdd

import com.tele.crm.data.network.model.activityAdd.ActivityAddResponse
import com.tele.crm.data.network.model.activityAdd.activtiyAddRequest
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface AddActivityUseCase : SuspendingUseCase<activtiyAddRequest, Result<ActivityAddResponse>>