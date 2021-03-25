package com.example.sjzs.model.http.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody

class HandleResponseInterceptor(val isNeedInterceptor: Boolean) : Interceptor {

    /**
     * 将无效的字符串去除
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())//向下分发request\

        if (isNeedInterceptor) {

            val bodyString = response.body?.string()

            val jsonString =
                bodyString?.substring(bodyString.indexOf(":") + 1, bodyString.lastIndexOf("}"))
            //重新构建ResponseBody
            val responseBody = ResponseBody.create("text/plain".toMediaType(), jsonString!!)
            return response.newBuilder().body(responseBody).build()

        }else{
            val jsonString =
                response.body?.string()
            //重新构建ResponseBody
           val responseBody = ResponseBody.create("text/plain".toMediaType(), jsonString!!)
            return response.newBuilder().body(responseBody).build()
        }
        Log.d("TAG1", response.body?.string()!!)

        return response
    }
}