package com.tele.crm.domain.usecases.login

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.model.auth.login.LoginRequest
import com.tele.crm.data.network.model.auth.login.LoginResponse
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.domain.usecases.login.LoginUseCase

class LoginUseCaseImpl(
    val webRepository: CRMRepository,
    private val appDataStore: AppDataStore

) : LoginUseCase {
     override suspend fun execute(input: LoginRequest): Result<LoginResponse> {
         val result= webRepository.login(input)
         saveUserData(result)

         return result

     }


    private suspend fun saveUserData(userDataResponse: Result<LoginResponse>): Result<LoginResponse> {
        userDataResponse.getOrNull()?.let { userData ->
            if (userData.success) {
                appDataStore.updateData {
                    it.copy(
                        isUserLoggedIn = true,
                        userId = userData.data?.user?._id.orEmpty(),
                        token = userData.data.token
                    )
                }
            }
        }
        return userDataResponse
    }
}