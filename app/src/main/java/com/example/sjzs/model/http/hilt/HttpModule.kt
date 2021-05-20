package com.example.sjzs.model.http.hilt

import com.example.sjzs.model.http.retrofit.RetrofitProcesser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * hilt提供对象的类
 */
@Module
@InstallIn(ApplicationComponent::class)//提供到哪
abstract class HttpModule {
    //多个binds系统分不清到底时哪个方法提供对象,需要注解
    @BindOkHttp
    @Binds//绑定,系统自动创建retrofitProcesser,并返回
    @Singleton
    abstract fun installOkHttp(retrofitProcesser: RetrofitProcesser):IHttpProcesser
}