package com.tele.crm.presentation.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.domain.entites.MetaItem
import com.tele.crm.domain.entites.UserItem
import com.tele.crm.domain.usecases.getProfile.GetProfileUseCase
import com.tele.crm.domain.usecases.logout.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel  @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    val appDataStore: AppDataStore,
    private val logOutUseCase: LogOutUseCase,

    ) : ViewModel() {

    var profileList: MutableList<UserItem> = mutableListOf()


    private val _profileListLiveData = MutableLiveData<List<UserItem>>()
    val profileListLiveData: LiveData<List<UserItem>> get() = _profileListLiveData


    fun getProfile() {
        viewModelScope.launch {
            if (profileList.isNotEmpty()) {
                _profileListLiveData.postValue(profileList)
            } else {
                getProfileUseCase.execute().onSuccess { response ->
                    if (response.success) {
                        profileList.clear()

                        val userItem = UserItem(
                            id = response.data._id.orEmpty(),
                            firstName = response.data.first_name.orEmpty(),
                            lastName = response.data.last_name.orEmpty(),
                            email = response.data.email_id.orEmpty(),
                            mobile = response.data.mobile.orEmpty()
                        )

                        profileList.add(userItem)

                        _profileListLiveData.postValue(profileList)
                    }
                }.onFailure { throwable ->

                    Log.e("GetProfileError", "Error fetching profile: ${throwable.message}", throwable)
                }
            }
        }
    }
    fun logOut() {
        viewModelScope.launch {
            logOutUseCase.execute()
        }
    }
}