package com.example.sjzs.model.http.retrofit

import com.example.network.api.base.NetworkApi
import com.example.sjzs.model.http.retrofit.interceptor.HandleResponseInterceptor
import okhttp3.Interceptor

class JsonHttp(baseUrl: String, isNeedInterceptor: Boolean) : NetworkApi(baseUrl) {
    private val mIsNeedInterceptor = isNeedInterceptor
    override fun getInterceptor(): Interceptor {

        return HandleResponseInterceptor(mIsNeedInterceptor)//用来处理无效字符串的拦截器

    }
}