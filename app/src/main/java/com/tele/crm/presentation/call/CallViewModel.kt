package com.tele.crm.presentation.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.CallEntry
import com.tele.crm.data.network.model.activityAdd.ActivityAddResponse
import com.tele.crm.data.network.model.activityAdd.activtiyAddRequest
import com.tele.crm.data.network.model.addLeadToCampaign.AddLeadToCampaignResponse
import com.tele.crm.data.network.model.addLeadToCampaign.AddToCampaignRequest
import com.tele.crm.data.network.model.callLogs.CallLogsResponse
import com.tele.crm.data.network.model.recentCalls.CallResponse
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkRequest
import com.tele.crm.data.network.model.updateRemark.UpdateRemarkResponse
import com.tele.crm.data.network.model.updateStatus.UpdateStatusRequest
import com.tele.crm.data.network.model.updateStatus.UpdateStatusResopnse
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.domain.usecases.activityAdd.AddActivityUseCase
import com.tele.crm.domain.usecases.addLeadToCampaign.AddLeadToCampaignUseCase
import com.tele.crm.domain.usecases.callLogs.GetCallLogUsecase
import com.tele.crm.domain.usecases.getLeads.GetLeadUseCase
import com.tele.crm.domain.usecases.recentCalls.RecentCallUseCase
import com.tele.crm.domain.usecases.removeLead.RemoveLeadUseCase
import com.tele.crm.domain.usecases.updateLeadStatus.UpdateStatusUseCase
import com.tele.crm.domain.usecases.updateRemark.UpdateRemarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    private val recentCallUseCase: RecentCallUseCase,
    private val callLogUsecase: GetCallLogUsecase,
    private val addLeadToCampaignUseCase: AddLeadToCampaignUseCase,
    private val removeLeadUseCase: RemoveLeadUseCase,
    private val updateLeadStatusUseCase: UpdateStatusUseCase,
    private val addActivityUseCase: AddActivityUseCase,
    private val updateRemarkUseCase: UpdateRemarkUseCase,

    ) :ViewModel(){

    private val _callEntries = MutableStateFlow<ApiCallingState<CallResponse>>(ApiCallingState.Idle)
    val callEntries: StateFlow<ApiCallingState<CallResponse>> get() = _callEntries.asStateFlow()

    private val _callLogs = MutableStateFlow<ApiCallingState<CallLogsResponse>>(ApiCallingState.Idle)
    val callLogs: StateFlow<ApiCallingState<CallLogsResponse>> get() = _callLogs.asStateFlow()

    private val _addLeadToCampaign = MutableStateFlow<ApiCallingState<AddLeadToCampaignResponse>>(ApiCallingState.Idle)
    val addLeadToCampaign: StateFlow<ApiCallingState<AddLeadToCampaignResponse>> get() = _addLeadToCampaign.asStateFlow()

    private val _removeLeadToCampaign = MutableStateFlow<ApiCallingState<AddLeadToCampaignResponse>>(ApiCallingState.Idle)
    val removeLeadToCampaign: StateFlow<ApiCallingState<AddLeadToCampaignResponse>> get() = _removeLeadToCampaign.asStateFlow()

    private val _updateLeadStatus = MutableStateFlow<ApiCallingState<UpdateStatusResopnse>>(ApiCallingState.Idle)
    val updateLeadStatus: StateFlow<ApiCallingState<UpdateStatusResopnse>> get() = _updateLeadStatus.asStateFlow()

    private val _addActivity = MutableStateFlow<ApiCallingState<ActivityAddResponse>>(ApiCallingState.Idle)
    val addActivity: StateFlow<ApiCallingState<ActivityAddResponse>> get() = _addActivity.asStateFlow()


    private val _updateRemark = MutableStateFlow<ApiCallingState<UpdateRemarkResponse>>(ApiCallingState.Idle)
    val updateRemark: StateFlow<ApiCallingState<UpdateRemarkResponse>> get() = _updateRemark.asStateFlow()

    fun getRecentCalls() {
        viewModelScope.launch {
            _callEntries.emit(ApiCallingState.Loading())
            recentCallUseCase.execute()
                ?.onSuccess {
                    _callEntries.emit(ApiCallingState.Success(it))
                }?.onFailure {
                    _callEntries.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }

    fun getCallLogs(leadId:String) {
        viewModelScope.launch {
            _callLogs.emit(ApiCallingState.Loading())
            callLogUsecase.execute(  GetCallLogUsecase.Input(leadId))
                .onSuccess {
                    _callLogs.emit(ApiCallingState.Success(it))
                }.onFailure {
                    _callLogs.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }

    fun addLeadToCampaignApi(addLeadToCampaignRequest: AddToCampaignRequest) {
        viewModelScope.launch {
            _addLeadToCampaign.emit(ApiCallingState.Loading())
            addLeadToCampaignUseCase.execute(
                addLeadToCampaignRequest
            ).onSuccess {
                _addLeadToCampaign.emit(ApiCallingState.Success(it))
            }.onFailure {
                _addLeadToCampaign.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }
    fun removeLeadToCampaignApi(addLeadToCampaignRequest: AddToCampaignRequest) {
        viewModelScope.launch {
            _removeLeadToCampaign.emit(ApiCallingState.Loading())
            removeLeadUseCase.execute(
                addLeadToCampaignRequest
            ).onSuccess {
                _removeLeadToCampaign.emit(ApiCallingState.Success(it))
            }.onFailure {
                _removeLeadToCampaign.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }
    fun addActivity(addRequest: activtiyAddRequest) {
        viewModelScope.launch {
            _addActivity.emit(ApiCallingState.Loading())
            addActivityUseCase.execute(
                addRequest
            ).onSuccess {
                _addActivity.emit(ApiCallingState.Success(it))
            }.onFailure {
                _addActivity.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }
    fun updateLeadStatus(updateStatusRequest: UpdateStatusRequest) {
        viewModelScope.launch {
            _updateLeadStatus.emit(ApiCallingState.Loading())
            updateLeadStatusUseCase.execute(
                updateStatusRequest
            ).onSuccess {
                _updateLeadStatus.emit(ApiCallingState.Success(it))
            }.onFailure {
                _updateLeadStatus.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }
    fun updateRemark(updateRemarkRequest: UpdateRemarkRequest) {
        viewModelScope.launch {
            _updateRemark.emit(ApiCallingState.Loading())
            updateRemarkUseCase.execute(
                updateRemarkRequest
            ).onSuccess {
                _updateRemark.emit(ApiCallingState.Success(it))
            }.onFailure {
                _updateRemark.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }

}