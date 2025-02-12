package com.tele.crm.data.network.repository

import com.tele.crm.data.network.ApiResponse
import com.tele.crm.data.network.ApiService
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddLeadToCampaignResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.model.callLogs.CallLogsResponse
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaign.CampaignResponse
import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.data.network.model.getInterest.GetInterestResponse
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.data.network.model.getStreams.GetStreamResponse
import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.logout.LogoutResponse
import com.tele.crm.data.network.model.profile.ProfileResponse
import com.tele.crm.data.network.model.recentCalls.CallResponse
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusResopnse
import com.tele.crm.data.network.response.NetworkResponse
import com.tele.crm.data.network.response.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class CRMRepositoryImpl @Inject constructor(  private val apiService: ApiService
) : CRMRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return apiService.login(request).toResult()

    }

    override suspend fun addLead(request: AddLeadRequest): Result<AddLeadResponse> {
        return apiService.addLead(request).toResult()
    }

    override suspend fun addLeadToCampaign(request: AddToCampaignRequest): Result<AddLeadToCampaignResponse> {
        return apiService.addLeadToCampaign(request).toResult()
    }

    override suspend fun updateLeadStatus(request: UpdateStatusRequest): Result<UpdateStatusResopnse> {
        return apiService.updateLadStatus(request).toResult()
    }

    override suspend fun getInterest(): Result<GetInterestResponse> {
        return apiService.getInterest().toResult()
    }

    override suspend fun getStream(): Result<GetStreamResponse> {
        return apiService.getStream().toResult()
    }

    override suspend fun getYear(): Result<GetYearResponse> {
        return  apiService.getYear().toResult()
    }

    override suspend fun getProfile(): Result<ProfileResponse> {
        return  apiService.getProfile().toResult()
    }

    override suspend fun getLeads(): Result<GetLeadResponse> {
        return  apiService.getLeads().toResult()
    }

    override suspend fun getCampaign(): Result<GetCampaignResponse> {
        return apiService.getCampaign().toResult()
    }

    override suspend fun logout(): Result<LogoutResponse> {
        return apiService.logout().toResult()
    }

    override suspend fun recentCalls(): Result<CallResponse> {
        return apiService.recentCalls().toResult()
    }

    override suspend fun getCallLogs(leadId:String): Result<CallLogsResponse> {
        return apiService.getCallLogs(leadId).toResult()
    }

    override suspend fun createCampaign(request: CampaignRequest): Result<CampaignResponse> {
        return apiService.createCampaign(request).toResult()
    }

    override suspend fun getCampaignDetails(campaignId: String): Result<CampaignDetailsResponse> {
        return apiService.getCampaignDetails(campaignId).toResult()
    }


}