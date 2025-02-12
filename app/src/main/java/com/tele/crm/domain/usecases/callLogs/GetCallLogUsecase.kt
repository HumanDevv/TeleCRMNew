package com.tele.crm.domain.usecases.callLogs

import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.callLogs.CallLogsResponse
import com.tele.crm.domain.usecases.SuspendingUseCase

interface GetCallLogUsecase : SuspendingUseCase<GetCallLogUsecase.Input, Result<CallLogsResponse>>{
    data class Input(val request: String)

}