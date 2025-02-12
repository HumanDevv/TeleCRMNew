package com.tele.crm.utils

import android.text.TextUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


object AppConstant {
    private const val BASE_URL_PRODUCTION: String = "https://api.praxoapp.com/api/"
    private const val BASE_URL_DEV: String = "https://gaugyan.org/"
    private const val BASE_URL_LOCAL: String = "http://192.168.1.41:8765/api/"
    private const val BASE_URL_QA: String = "http://13.233.82.204:8765/api/"
    const val BASE_URL: String = BASE_URL_DEV
    const val READ_TIME_OUT = 60L
    const val WRITE_TIME_OUT = 60L
    const val CONNECT_TIME_OUT = 60L

    const val APP_JSON = "application/json"

    const val PRIVACY_POLICY = "privacy-policy"
    const val TERMS_CONDITION = "term-conditions"
    const val ABOUT_US = "about-us"
    const val CONTACT_US = "contact-us"
    const val LEGAL_POLICY = "legal-policy"


    const val REPORT_PROFILE = "PROFILE"
    const val REPORT_VIDEO = "VIDEO"
    const val HTTP_RESPONSE_CODE_409 = 409
    const val HTTP_RESPONSE_CODE_401 = 401
    const val HTTP_RESPONSE_CODE_403 = 403
    const val HTTP_RESPONSE_CODE_413 = 413
    const val HTTP_RESPONSE_CODE_502 = 502

    //DeepLinking
    const val DYNAMIC_LINK_URL = "https://thepraxo.page.link"
    const val DYNAMIC_LINK_POST_PREFIX = "post/"
    const val DYNAMIC_LINK_CHALLENGE_PREFIX = "challenge/"
    const val DYNAMIC_LINK_PROFILE_PREFIX = "profile/"
    const val DYNAMIC_LINK_AUDIO_PREFIX = "audio/"
    const val DYNAMIC_LINK_VIDEO_PREFIX = "video/"
    const val DYNAMIC_LINK_REFERRAL_PREFIX = "referral/"

    const val SINGLE_VIDEO = "SINGLE_VIDEO"
    const val SINGLE_AD = "SINGLE_AD"
    const val currencySymbol = "₹"

    fun isSameDay(lastRewardedDate: String?, currentDayTimestamp: Long): Boolean {

        var lastRewardedTimeStamp = 0L
        val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        try {
            lastRewardedTimeStamp = fromFormat.parse(lastRewardedDate).time

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date1: String = sdf.format(Date(lastRewardedTimeStamp))
        val date2: String = sdf.format(Date(currentDayTimestamp))
        return date1 == date2
    }

    fun isSameDay(lastData: String?, compareDate: String): Boolean {

        var lastRewardedTimeStamp = 0L
        val fromFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            lastRewardedTimeStamp = fromFormat.parse(lastData).time

        } catch (e: Exception) {
            e.printStackTrace()
        }

        var compareTimeStamp = 0L
        val compareFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        try {
            compareTimeStamp = compareFormat.parse(compareDate).time

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date1: String = sdf.format(Date(lastRewardedTimeStamp))
        val date2: String = sdf.format(Date(compareTimeStamp))
        return date1 == date2
    }

    fun dateConverter(sourceFormat: String?, destFormat: String?, dateData: String?): String? {
        return try {
            var resultDate: String? = null
            if (!TextUtils.isEmpty(dateData)) {
                val srcFormat: java.text.DateFormat =
                    SimpleDateFormat(sourceFormat, Locale.getDefault())
                val date = dateData?.let { srcFormat.parse(it) }
                val destFormats: java.text.DateFormat =
                    SimpleDateFormat(destFormat, Locale.getDefault())
                resultDate = date?.let { destFormats.format(it) }
            }
            resultDate
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    fun getCurrentDateInSpecificFormat1(
        currentCalDate: String?,
        formate1: String,
        formate2: String
    ): String {
        return if (currentCalDate.isNullOrEmpty()) {
            ""
        } else {
            val oldFormatter = SimpleDateFormat(formate1)
            oldFormatter.timeZone = TimeZone.getTimeZone("UTC")
            val value: Date?
            var dueDateAsNormal = ""
            try {
                value = oldFormatter.parse(currentCalDate)
                val newFormatter = SimpleDateFormat(formate2)
                newFormatter.timeZone = TimeZone.getDefault()
                dueDateAsNormal = newFormatter.format(value)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            Log.d(
                "TimeData",
                "CurrentDate $currentCalDate \n SourceFormat $formate1 \n DesFormat $formate2 \n Return Value $dueDateAsNormal"
            )
            dueDateAsNormal
        }
    }

    const val ACTION_CLICK_MORE = "com.app.praxo.CLICK_ACTION"

}