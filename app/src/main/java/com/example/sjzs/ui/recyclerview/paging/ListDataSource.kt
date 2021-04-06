package com.example.sjzs.ui.recyclerview.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.example.sjzs.model.DataManager
import com.example.sjzs.model.bean.CommonData
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.model.bean.beanmanager.BeanManager
import com.example.sjzs.model.observer.ResponseObserver

class ListDataSource : PageKeyedDataSource<Int, ListBean>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ListBean>
    ) {
        DataManager.requestCommonData(
            "china",
            "1",
            object : ResponseObserver<CommonData>() {
                override fun onSuccess(t: CommonData) {
                    callback.onResult(t.list, null, 2)
                }

                override fun onFailure(e: Throwable) {
                    Log.d("OkHttp", "onFailure: ")
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ListBean>) {
        Log.d("onLoadAfter", "${params.key}")
        DataManager.requestCommonData(
            "china",
            "${params.key}",
            object : ResponseObserver<CommonData>() {
                override fun onSuccess(t: CommonData) {
                    callback.onResult(t.list, params.key+1)
                }

                override fun onFailure(e: Throwable) {
                    Log.d("OkHttp", "onFailure: ")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ListBean>) {

    }
}