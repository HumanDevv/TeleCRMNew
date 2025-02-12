package com.tele.crm.data.network.response

import okhttp3.ResponseBody
import java.io.IOException

@Suppress("UNUSED_PARAMETER")
class HttpException(statusCode: Int,
                    rawBody: ResponseBody?,
                    errorMessage: String?) : IOException("$errorMessage")
