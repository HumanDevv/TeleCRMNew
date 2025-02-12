package com.tele.crm.data.datastore.di

import android.content.Context
import com.tele.crm.data.datastore.appDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDataStoreModule {
    @Singleton
    @Provides
    fun provideAppDataStore(@ApplicationContext context: Context) = context.appDataStore
}
