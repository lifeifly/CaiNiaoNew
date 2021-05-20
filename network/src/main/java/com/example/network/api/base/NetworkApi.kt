package com.example.network.api.base

import com.example.network.api.observer.BaseObserver
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit

abstract class NetworkApi(baseUrl: String) {
    //支持多域名
    private val mBaseUrl = baseUrl

    companion object {
        //retrofit复用
        private val mRetrofits = hashMapOf<String, Retrofit>()
        private val mNoConvertRetrofit= hashMapOf<String,Retrofit>()
    }

    /**
     * 暴露给用户使用
     */
    fun <T> getService(service: Class<T>): T {
        return getRetrofit(service).create(service)
    }


    fun <T> getRetrofit(service: Class<T>): Retrofit {
//        if (mRetrofits.get(mBaseUrl + service.name) != null) {
//            //有缓存
//            return mRetrofits.get(mBaseUrl + service.name)!!
//        }

        //无缓存
        val retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())//Gson转化
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//转换成RxjavaObservable
            .baseUrl(mBaseUrl)
            .build()
        //放入缓存
//        mRetrofits.put(mBaseUrl + service.name, retrofit)

        return retrofit
    }

    fun <T> getNoConvertRetrofit(service: Class<T>): Retrofit {
        if (mNoConvertRetrofit.get(mBaseUrl + service.name) != null) {
            //有缓存
            return mNoConvertRetrofit.get(mBaseUrl + service.name)!!
        }

        //无缓存
        val retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//转换成RxjavaObservable
            .baseUrl(mBaseUrl)
            .build()
        //放入缓存
        mNoConvertRetrofit.put(mBaseUrl + service.name, retrofit)

        return retrofit
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        //添加用户自定义的拦截器
        okHttpClientBuilder.addInterceptor(getInterceptor())
        okHttpClientBuilder.connectTimeout(6000,TimeUnit.MILLISECONDS)
        //打印日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        return okHttpClientBuilder.build()
    }

    //将操作合并
    fun <T> applySchedulers(baseObserver: BaseObserver<T>): ObservableTransformer<T, T> {
        val observableTransformer = object : ObservableTransformer<T, T> {
            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                val observable = upstream
                    .subscribeOn(Schedulers.io())//网络操作在子线程
                    .observeOn(AndroidSchedulers.mainThread())//之后的操作在主线程

                observable.subscribe(baseObserver)

                return observable
            }
        }
        return observableTransformer
    }

    //模板设计模式，如果需要给外界实现
    abstract protected fun getInterceptor(): Interceptor
}