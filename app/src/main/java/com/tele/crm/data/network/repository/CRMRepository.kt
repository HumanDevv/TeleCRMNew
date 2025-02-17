package com.tele.crm.data.network.repository

import com.tele.crm.data.network.model.activityAdd.ActivityAddResponse
import com.tele.crm.data.network.model.activityAdd.activtiyAddRequest
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
import com.tele.crm.data.network.model.updateCampaign.UpdateCampaignResponse
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkRequest
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkResponse
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusResopnse

interface CRMRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun addLead(request: AddLeadRequest): Result<AddLeadResponse>
    suspend fun removeLeadToCampaign(request: AddToCampaignRequest): Result<AddLeadToCampaignResponse>
    suspend fun updateLead(leadId:String,request: AddLeadRequest): Result<AddLeadResponse>

    suspend fun addLeadToCampaign(request: AddToCampaignRequest): Result<AddLeadToCampaignResponse>
    suspend fun addActivity(request: activtiyAddRequest): Result<ActivityAddResponse>
    suspend fun updateLeadStatus(request: UpdateStatusRequest): Result<UpdateStatusResopnse>
    suspend fun getInterest(): Result<GetInterestResponse>
    suspend fun getStream(): Result<GetStreamResponse>
    suspend fun getYear(): Result<GetYearResponse>
    suspend fun getProfile(): Result<ProfileResponse>
    suspend fun getLeads(): Result<GetLeadResponse>
    suspend fun getCampaign(): Result<GetCampaignResponse>
    suspend fun logout(): Result<LogoutResponse>
    suspend fun recentCalls(): Result<CallResponse>
    suspend fun getCallLogs(leadId:String): Result<CallLogsResponse>
    suspend fun createCampaign(request: CampaignRequest): Result<CampaignResponse>
    suspend fun updateRemark(request: UpdateRemarkRequest): Result<UpdateRemarkResponse>
    suspend fun getCampaignDetails(campaignId:String): Result<CampaignDetailsResponse>
    suspend fun updateCampaign(campaignId:String,request: CampaignRequest): Result<UpdateCampaignResponse>
}