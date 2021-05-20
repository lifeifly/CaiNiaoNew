package com.example.sjzs.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.bean.RollData
import com.example.sjzs.model.bean.VideoList
import com.example.sjzs.model.http.VIDEO_BASE_URL
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import com.example.sjzs.model.paging.ChinaListRemoteMediator
import com.example.sjzs.model.paging.ListDataSource
import com.example.sjzs.model.paging.VideoListRemoteMediator
import com.example.sjzs.model.room.ListBeanDatabase
import retrofit2.create


class VideoDataViewModel(application: Application) : AndroidViewModel(application) {
    //广告数据
    val bannerList:MutableLiveData<List<VideoList>>
    private val database=ListBeanDatabase.getDatabase(application)
    val retrofit=JsonHttp(VIDEO_BASE_URL,true)
    val apisService=retrofit.getService(ApiService::class.java)
    val config = PagingConfig(20, 1, false, 60)

    //列表数据
    val listData = Pager(
        config = config
        ,remoteMediator = VideoListRemoteMediator("video",database,apisService)
    )
    {
        database.videoListDao().queryAllListBeans()
    }
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()

    init {
        bannerList=MutableLiveData()
    }
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