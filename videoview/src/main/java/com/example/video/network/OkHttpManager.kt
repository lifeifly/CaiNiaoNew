package com.example.video.network

import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit

object OkHttpManager {
   private val okHttpClient = OkHttpClient()
            .newBuilder().readTimeout(2000,TimeUnit.MILLISECONDS)
            .connectTimeout(2000,TimeUnit.MILLISECONDS)
            .writeTimeout(2000,TimeUnit.MILLISECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .build()
    fun async(url: String): Call {
        val request = Request.Builder()
                .url(url)
                .build()
        return okHttpClient.newCall(request)
    }

    fun syncResponse(url: String, start: Long, end: Long): Response {
        val request = Request.Builder()
                .addHeader("Accept-Ranges", "bytes=" + start + "-" + end)
                .url(url)
                .build()

        return okHttpClient.newCall(request).execute()
    }
}