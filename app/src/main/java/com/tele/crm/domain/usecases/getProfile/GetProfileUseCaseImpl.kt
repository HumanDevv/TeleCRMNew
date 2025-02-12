package com.tele.crm.domain.usecases.getProfile

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.network.model.getYear.GetYearResponse
import com.tele.crm.data.network.model.profile.ProfileResponse
import com.tele.crm.data.network.repository.CRMRepository

class GetProfileUseCaseImpl(
    val webRepository: CRMRepository,
    private val appDataStore: AppDataStore
) : GetProfileUseCase {


    override suspend fun execute(): Result<ProfileResponse> {
        val result= webRepository.getProfile()
        saveUserData(result)
        return result
    }
    private suspend fun saveUserData(result:  Result<ProfileResponse>): Result<ProfileResponse> {
        result.getOrNull()?.let { results ->
            appDataStore.updateData { appData ->
                appData.copy(
                    isUserLoggedIn = true,
                    userId = results.data._id.orEmpty(),
                    email = results.data.email_id.orEmpty(),
                    firstName = results.data.first_name.orEmpty(),
                    lastName = results.data.last_name.toString(),
                    mobile = results.data.mobile.toString()

                )
            }
        }
        return result
    }
}