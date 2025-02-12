package com.tele.crm.domain.usecases.updateLeadStatus

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusResopnse
import com.tele.crm.domain.usecases.SuspendingUseCase


interface UpdateStatusUseCase : SuspendingUseCase<UpdateStatusRequest, Result<UpdateStatusResopnse>>