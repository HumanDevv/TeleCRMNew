package com.tele.crm.domain.usecases.callLogs

import com.tele.crm.data.network.model.callLogs.CallLogsResponse
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.getLeads.GetLeadUseCase

class GetCallLogUseCaseImpl (
    val webRepository: CRMRepository,
) : GetCallLogUsecase {


    override suspend fun execute(leadId:GetCallLogUsecase.Input): Result<CallLogsResponse> {
        val result= webRepository.getCallLogs(leadId.request )
        return result
    }

}
