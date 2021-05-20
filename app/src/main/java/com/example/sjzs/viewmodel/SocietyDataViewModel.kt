package com.example.sjzs.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.sjzs.model.http.LIST_BASE_URL

import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import com.example.sjzs.model.paging.ListDataSource
import com.example.sjzs.model.paging.SocietyListRemoteMediator
import com.example.sjzs.model.paging.WorldListRemoteMediator
import com.example.sjzs.model.room.ListBeanDatabase


class SocietyDataViewModel(application: Application) : AndroidViewModel(application) {
    //数据库
    val database = ListBeanDatabase.getDatabase(getApplication())
    val societyListDao = database.societyListDao()

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
       remoteMediator = SocietyListRemoteMediator("society","society", database, apiService)
    )
    {
        societyListDao.queryAllListBeans()
    }
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()

}