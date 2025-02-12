package com.tele.crm.presentation.lead

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.getLeads.GetLeadResponse
import com.tele.crm.domain.usecases.getLeads.GetLeadUseCase
import com.tele.crm.utils.extension.updateList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeadViewModel @Inject constructor(
    private val getLeadUseCase: GetLeadUseCase

    ) :ViewModel(){


    private val _leadEntries =
        MutableStateFlow<ApiCallingState<GetLeadResponse>>(ApiCallingState.Idle)
    val leadEnteries = _leadEntries.asSharedFlow()


    fun getLeads() {
        viewModelScope.launch {
            _leadEntries.emit(ApiCallingState.Loading())
            getLeadUseCase.execute()
                ?.onSuccess {
                    _leadEntries.emit(ApiCallingState.Success(it))
                }?.onFailure {
                    _leadEntries.emit(ApiCallingState.Failure.Unknown(throwable = it))
                }
        }
    }



}