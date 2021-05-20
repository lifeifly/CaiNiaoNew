package com.example.sjzs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.http.LIST_BASE_URL
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import com.example.sjzs.model.paging.ChinaListRemoteMediator
import com.example.sjzs.model.room.ListBeanDatabase


class ChinaDataViewModel(application: Application) : AndroidViewModel(application) {
    //数据库
    val database = ListBeanDatabase.getDatabase(getApplication())
    val listBeanDao = database.chinaListDao()

    //广告数据
    val bannerData: MutableLiveData<List<CommomBanner>> = MutableLiveData()
    val config = PagingConfig(20, 1, false, 60)
    val userHttp =
        JsonHttp(
            LIST_BASE_URL,
            true
        )
    val apiService = userHttp.getService(ApiService::class.java)

    //列表数据
    val listData = Pager(
        config = config,
        remoteMediator = ChinaListRemoteMediator("china","china", database, apiService)
    )
    {
        listBeanDao.queryAllListBeans()
    }
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()

//    //存放后续加载状态的livedata
//    val afterNetwordStatus =
//        Transformations.switchMap(dataSourceFactory.listDataSource) { it.afterNetwordStatus }
//    //存放首次加的状态的livedata
//    val initialNetworkStatus =
//        Transformations.switchMap(dataSourceFactory.listDataSource) { it.initialNetworkStatus }

//    /**
//     * 后续请求失败重新尝试
//     */
//    fun retryAfter() {
//        //重新执行上次失败的方法
//        dataSourceFactory.listDataSource.value?.afterRetry?.invoke()
//    }
//    /**
//     * 初次请求失败重新尝试
//     */
//    fun retryInitial(){
//        dataSourceFactory.listDataSource.value?.initialRetry?.invoke()
//    }

}