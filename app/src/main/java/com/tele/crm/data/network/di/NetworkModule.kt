package com.tele.crm.data.network.di

import android.util.Log
import com.tele.crm.BuildConfig
import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.datastore.getData
import com.tele.crm.data.network.ApiService
import com.tele.crm.domain.appevents.AppEventsHandler
import com.tele.crm.data.network.repository.CRMRepository
import com.tele.crm.data.network.repository.CRMRepositoryImpl
import com.tele.crm.data.network.response.NetworkResponseCallAdapterFactory
import com.tele.crm.domain.appevents.AppEventsHandlerImpl
import com.tele.crm.utils.AppConstant
import com.tele.crm.utils.AppConstant.CONNECT_TIME_OUT
import com.tele.crm.utils.AppConstant.READ_TIME_OUT
import com.tele.crm.utils.AppConstant.WRITE_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideAppEventsHandler(): AppEventsHandler = AppEventsHandlerImpl()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        @Named("baseURL") baseURL: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addCallAdapterFactory(NetworkResponseCallAdapterFactory)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @Named("httpLoggingInterceptor")
        httpLoggingInterceptor: Interceptor,
        authInterceptor: AuthenticateInterceptor,

    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        addInterceptor(authInterceptor)
        addInterceptor(httpLoggingInterceptor)
    }.build()


    @Singleton
    @Provides
    @Named("OkHttpClientWithoutAuth")
    fun provideOkHttpClientWithoutAuth(
        @Named("httpLoggingInterceptor")
        httpLoggingInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(httpLoggingInterceptor)
        readTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
    }.build()




    @Singleton
    @Provides
    @Named("httpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        appDataStore: AppDataStore, appEventsHandler: AppEventsHandler,

    ) = AuthenticateInterceptor(appDataStore, appEventsHandler)
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCRMRepository(apiService: ApiService): CRMRepository {
        return CRMRepositoryImpl(apiService)
    }


    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    @Named("baseURL")
    fun provideApiBaseURL(): String = AppConstant.BASE_URL
}
