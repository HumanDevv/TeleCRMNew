package com.tele.crm.data.network

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("user/login")
    suspend fun login(@Body loginRequest: LoginRequest): NetworkResponse<LoginResponse>

    @POST("lead/user/add")
    suspend fun addLead(@Body addLeadRequest: AddLeadRequest): NetworkResponse<AddLeadResponse>


    @POST("campaign/user/add")
    suspend fun createCampaign(@Body campaignRequest: CampaignRequest): NetworkResponse<CampaignResponse>


    @POST("campaign/user/add-lead")
    suspend fun addLeadToCampaign(@Body addToCampaignRequest: AddToCampaignRequest): NetworkResponse<AddLeadToCampaignResponse>



    @PATCH("lead/user/update-status")
    suspend fun updateLadStatus(@Body updateStatusRequest: UpdateStatusRequest): NetworkResponse<UpdateStatusResopnse>


    @GET("user/get-interests")
    suspend fun getInterest(): NetworkResponse<GetInterestResponse>

    @GET("user/get-streams")
    suspend fun getStream(): NetworkResponse<GetStreamResponse>

    @GET("user/get-years")
    suspend fun getYear(): NetworkResponse<GetYearResponse>

    @GET("user/profile")
    suspend fun getProfile(): NetworkResponse<ProfileResponse>
    @GET("campaign/user/get")
    suspend fun getCampaign(): NetworkResponse<GetCampaignResponse>

    @GET("lead/user/get")
    suspend fun getLeads(): NetworkResponse<GetLeadResponse>

    @GET("activity/user/recent-calls")
    suspend fun recentCalls(): NetworkResponse<CallResponse>

    @POST("user/logout")
    suspend fun logout(): NetworkResponse<LogoutResponse>

    @GET("activity/user/call-logs/{id}")
    suspend fun getCallLogs(@Path("id") callLogId: String): NetworkResponse<CallLogsResponse>

    @GET("campaign/user/get-details/{id}")
    suspend fun getCampaignDetails(@Path("id") campaignId: String): NetworkResponse<CampaignDetailsResponse>
}