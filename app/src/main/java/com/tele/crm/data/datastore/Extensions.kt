package com.tele.crm.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.first

typealias AppDataStore = DataStore<AppDataStoreDto>

val Context.appDataStore by dataStore("app-data-store.json", AppDataStoreSerializer)

suspend fun AppDataStore.getData(): AppDataStoreDto = this.data.first()


suspend fun AppDataStore.clear(): AppDataStoreDto = this.updateData {
    it.copy(
        isUserLoggedIn = false,
        userId = "",
        firstName = "",
        lastName = "",
        mobile = "",

    )
}