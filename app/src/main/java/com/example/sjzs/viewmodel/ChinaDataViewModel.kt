package com.example.sjzs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.recyclerview.paging.ListDataSourceFactory
import com.example.sjzs.viewmodel.base.BaseDataVM
import java.util.concurrent.Executors

class ChinaDataViewModel : BaseDataVM() {

    val listData: LiveData<PagedList<ListBean>>//列表数据
    val bannerData: MutableLiveData<List<CommomBanner>>

    init {
        val config=PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(60)
            .setPrefetchDistance(1)
            .setEnablePlaceholders(false)
            .setMaxSize(250)
            .build()
        listData=LivePagedListBuilder(ListDataSourceFactory(),config).setFetchExecutor(Executors.newFixedThreadPool(2)).build()
        bannerData = MutableLiveData()
    }
}