package com.example.sjzs

import android.app.Application
import android.util.Log
import com.example.sjzs.model.datastore.datastore
import com.example.sjzs.model.datastore.key
import com.example.sjzs.model.http.hilt.BindOkHttp
import com.example.sjzs.model.http.hilt.HttpHelper
import com.example.sjzs.model.http.hilt.IHttpProcesser
import com.example.sjzs.model.http.retrofit.RetrofitProcesser
import com.example.skin.SkinManager
import dagger.hilt.android.HiltAndroidApp
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp//hilt注入，必须的
class MyApplication : Application() {
    @BindOkHttp
    @Inject lateinit var iHttpProcesser:IHttpProcesser

    override fun onCreate() {
        super.onCreate()
        //初始化换肤控件
        SkinManager.init(applicationContext)

        //获取skin的flow对象，不会立即执行，被订阅后才执行，因为是冷流
        //定义一个协程
        GlobalScope.launch(Dispatchers.IO) {
                datastore.data.map {
                    Log.d("TAG1", "onCreate: 2"+Thread.currentThread())
                    //获取属性
                    it[key] ?: ""
                }.collect{
                    Log.d("TAG1", "onCreate: "+Thread.currentThread())
                    SkinManager.loadSkinApk(it)
                }
        }
//使用了hilt，不用这个对象了
//        HttpHelper.init(RetrofitProcesser())
    }


}