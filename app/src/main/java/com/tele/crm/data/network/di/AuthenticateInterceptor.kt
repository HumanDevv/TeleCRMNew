package com.tele.crm.data.network.di

import com.tele.crm.data.datastore.AppDataStore
import com.tele.crm.data.datastore.getData
import com.tele.crm.domain.appevents.AppEventsHandler
import com.tele.crm.utils.AppConstant
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticateInterceptor @Inject constructor(
    private val appDataStore: AppDataStore,
    private val appEventsHandler: AppEventsHandler,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val authenticationRequest = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)

        var token: String
        var isUserLogin: Boolean

        val mCalendar: Calendar = GregorianCalendar()
        val mTimeZone: TimeZone = mCalendar.getTimeZone()
        val mGMTOffset = mTimeZone.rawOffset
        val offSet = TimeUnit.MINUTES.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS).toString()

        runBlocking {
            token = appDataStore.getData().token
            isUserLogin = appDataStore.getData().isUserLoggedIn
        }

        if (isUserLogin) {
            authenticationRequest.addHeader("authorization", "Bearer $token")
        }

        authenticationRequest.addHeader("offSet", offSet)

        val origResponse = chain.proceed(authenticationRequest.build())

        when (origResponse.code) {
            AppConstant.HTTP_RESPONSE_CODE_401 -> {
                return logoutEvent(origResponse)
            }

            AppConstant.HTTP_RESPONSE_CODE_413 -> {
                runBlocking {
                    appEventsHandler.request413()
                }
            }

            AppConstant.HTTP_RESPONSE_CODE_403 -> {
                return logoutEvent(origResponse)
            }

            AppConstant.HTTP_RESPONSE_CODE_502 -> {
                runBlocking {
                    appEventsHandler.request502()
                }
            }
        }

        return origResponse
    }

    private fun logoutEvent(origResponse: Response): Response {
        runBlocking { appEventsHandler.logout() }
        return origResponse
    }
}