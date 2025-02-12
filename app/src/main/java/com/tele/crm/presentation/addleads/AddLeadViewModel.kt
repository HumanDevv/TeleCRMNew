package com.tele.crm.presentation.addleads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.model.addLead.AddLeadRequest
import com.tele.crm.data.network.model.addLead.AddLeadResponse
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.domain.usecases.addLead.AddLeadUseCase
import com.tele.crm.domain.usecases.getInterest.GetInterestUseCase
import com.tele.crm.domain.usecases.getStream.GetStreamUseCase
import com.tele.crm.domain.usecases.getProfile.GetProfileUseCase
import com.tele.crm.domain.usecases.getYears.GetYearUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLeadViewModel @Inject constructor(
    private val addLeadUseCase: AddLeadUseCase,
    private val getInterestUseCase: GetInterestUseCase,
    private val getStreamUseCase: GetStreamUseCase,
    private val getYearUseCase: GetYearUseCase,
) : ViewModel() {
    val name = MutableLiveData<String>()
    val mobile = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val collegeName = MutableLiveData<String>()
    var interestList: MutableList<MetaItem> = mutableListOf()
    var streamList: MutableList<MetaItem> = mutableListOf()
    var yearList: MutableList<MetaItem> = mutableListOf()

    fun isNameValid(): Boolean = !name.value.isNullOrEmpty()

    fun isMobileValid(): Boolean =
        !mobile.value.isNullOrEmpty() && mobile.value!!.length == 10

    fun isEmailValid(): Boolean =
        !email.value.isNullOrEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()

    fun isAddressValid(): Boolean = !address.value.isNullOrEmpty()

    fun isCollegeNameValid(): Boolean = !collegeName.value.isNullOrEmpty()

    fun isFormValid(): Boolean {
        return isNameValid() && isMobileValid() && isEmailValid() && isAddressValid() && isCollegeNameValid()
    }


    private val _addLeadFlow = MutableStateFlow<ApiCallingState<AddLeadResponse>>(
        ApiCallingState.Idle
    )
    val addLeadFlow: SharedFlow<ApiCallingState<AddLeadResponse>> get() = _addLeadFlow.asSharedFlow()

    private val _interestListLiveData = MutableLiveData<List<MetaItem>>()
    val interestListLiveData: LiveData<List<MetaItem>> get() = _interestListLiveData

    private val _streamListLiveData = MutableLiveData<List<MetaItem>>()
    val streamListLiveData: LiveData<List<MetaItem>> get() = _streamListLiveData

    private val _yearListLiveData = MutableLiveData<List<MetaItem>>()
    val yearListLiveData: LiveData<List<MetaItem>> get() = _yearListLiveData

    fun addLeadApi(addLeadRequest: AddLeadRequest) {
        viewModelScope.launch {
            _addLeadFlow.emit(ApiCallingState.Loading())
            addLeadUseCase.execute(
                addLeadRequest
            ).onSuccess {
                _addLeadFlow.emit(ApiCallingState.Success(it))
            }.onFailure {
                _addLeadFlow.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }

    fun getInterest() {
        viewModelScope.launch {
            if (interestList.isNotEmpty()) {
                // Emit from local cache if already fetched
                _interestListLiveData.postValue(interestList)
            } else {
                getInterestUseCase.execute().onSuccess { response ->
                    if (response.success) {
                        interestList.clear()
                        val tempList = response.data.map { item ->
                            MetaItem(
                                id = item._id.orEmpty(),
                                title = item.interest.orEmpty()
                            )
                        }
                        interestList.addAll(tempList)
                        _interestListLiveData.postValue(interestList)
                    }
                }.onFailure {
                    // Handle failure (log or notify the user if needed)
                }
            }
        }
    }
    fun getStream() {
        viewModelScope.launch {
            if (streamList.isNotEmpty()) {
                // Emit from local cache if already fetched
                _streamListLiveData.postValue(streamList)
            } else {
                getStreamUseCase.execute().onSuccess { response ->
                    if (response.data.isNotEmpty()) {
                        streamList.clear()
                        val tempList = response.data.map { item ->
                            MetaItem(
                                id = item._id.orEmpty(),
                                title = item.stream_name.orEmpty()
                            )
                        }
                        streamList.addAll(tempList)
                        _streamListLiveData.postValue(streamList)
                    }
                }.onFailure {
                    // Handle failure (log or notify the user if needed)
                }
            }
        }
    }
    fun getYear() {
        viewModelScope.launch {
            if (yearList.isNotEmpty()) {
                // Emit from local cache if already fetched
                _yearListLiveData.postValue(yearList)
            } else {
                getYearUseCase.execute().onSuccess { response ->
                    if (response.data.isNotEmpty()) {
                        yearList.clear()
                        val tempList = response.data.map { item ->
                            MetaItem(
                                id = item._id.orEmpty(),
                                title = item.year.orEmpty()
                            )
                        }
                        yearList.addAll(tempList)
                        _yearListLiveData.postValue(yearList)
                    }
                }.onFailure {
                    // Handle failure (log or notify the user if needed)
                }
            }
        }
    }


}