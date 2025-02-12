package com.tele.crm.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.network.ApiCallingState
import com.tele.crm.data.network.ApiResponse
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.repository.CRMRepositoryImpl
import com.tele.crm.domain.usecases.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<ApiCallingState<LoginResponse>>(
        ApiCallingState.Idle
    )
    val loginFlow: SharedFlow<ApiCallingState<LoginResponse>> get() = _loginFlow.asSharedFlow()

    fun loginApi(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _loginFlow.emit(ApiCallingState.Loading())
            loginUseCase.execute(
                loginRequest
            ).onSuccess {
                _loginFlow.emit(ApiCallingState.Success(it))
            }.onFailure {
                _loginFlow.emit(ApiCallingState.Failure.Unknown(throwable = it))
            }
        }
    }
}