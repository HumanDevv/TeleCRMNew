package com.tele.crm.presentation.campaign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.campaign.CampaignRequest
import com.tele.crm.data.network.model.campaign.CampaignResponse
import com.tele.crm.data.network.model.campaignDetails.CampaignDetailsResponse
import com.tele.crm.data.network.model.getCampaign.GetCampaignResponse
import com.tele.crm.data.network.model.updateCampaign.UpdateCampaignResponse
import com.tele.crm.domain.usecases.createCampaign.CreateCampaignUseCase
import com.tele.crm.domain.usecases.getCampaign.GetCampaignUseCase
import com.tele.crm.domain.usecases.getCampaignDetails.GetCampaignDetailsUseCase
import com.tele.crm.domain.usecases.updateCampaign.UpdateCampaignUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignViewModel @Inject constructor(
    private val campaignUseCase: GetCampaignUseCase,
    private val createCampaignUseCase: CreateCampaignUseCase,
    private val getCampaignDetailsUseCase: GetCampaignDetailsUseCase,
    private val updateCampaignUseCase: UpdateCampaignUseCase
): ViewModel() {

    private val _campaignEntries = MutableStateFlow<ApiCallingState<CampaignResponse>>(ApiCallingState.Idle)
    val campaignEntries: SharedFlow<ApiCallingState<CampaignResponse>> get() = _campaignEntries.asSharedFlow()

    private val _getCampaigns = MutableStateFlow<ApiCallingState<GetCampaignResponse>>(ApiCallingState.Idle)
    val getCampaigns: StateFlow<ApiCallingState<GetCampaignResponse>> get() = _getCampaigns.asStateFlow()

    private val _getCampaignDetails = MutableStateFlow<ApiCallingState<CampaignDetailsResponse>>(ApiCallingState.Idle)
    val getCampaignDetails: StateFlow<ApiCallingState<CampaignDetailsResponse>> get() = _getCampaignDetails.asStateFlow()


    private val _updateCampaignDetails = MutableStateFlow<ApiCallingState<UpdateCampaignResponse>>(ApiCallingState.Idle)
    val updateCampaignDetails: StateFlow<ApiCallingState<UpdateCampaignResponse>> get() = _updateCampaignDetails.asStateFlow()


    fun addCampaignEntry(entry: CampaignRequest) {
        viewModelScope.launch {
            _campaignEntries.emit(ApiCallingState.Loading())
            createCampaignUseCase.execute(
                entry
            ).onSuccess {
                _campaignEntries.emit(ApiCallingState.Success(it))
            }.onFailure {
                _campaignEntries.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }

    fun getCampaigns() {
        viewModelScope.launch {
            _getCampaigns.emit(ApiCallingState.Loading())
            campaignUseCase.execute()
                ?.onSuccess {
                    _getCampaigns.emit(ApiCallingState.Success(it))
                }?.onFailure {
                    _getCampaigns.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }

    fun getCampaignDetails(campaignId:String) {
        viewModelScope.launch {
            _getCampaignDetails.emit(ApiCallingState.Loading())
            getCampaignDetailsUseCase.execute(  GetCampaignDetailsUseCase.Input(campaignId))
                .onSuccess {
                    _getCampaignDetails.emit(ApiCallingState.Success(it))
                }.onFailure {
                    _getCampaignDetails.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }
    fun updateCampaignDetails(campaignId:String,request: CampaignRequest) {
        viewModelScope.launch {
            _updateCampaignDetails.emit(ApiCallingState.Loading())
            updateCampaignUseCase.execute(  UpdateCampaignUseCase.Input(campaignId,request))
                .onSuccess {
                    _updateCampaignDetails.emit(ApiCallingState.Success(it))
                }.onFailure {
                    _updateCampaignDetails.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }

}