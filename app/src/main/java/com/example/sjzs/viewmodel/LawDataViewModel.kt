package com.example.sjzs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.sjzs.model.http.LIST_BASE_URL
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import com.example.sjzs.model.paging.LawListRemoteMediator
import com.example.sjzs.model.room.ListBeanDatabase


class LawDataViewModel(application: Application) : AndroidViewModel(application) {
    //数据库
    val database = ListBeanDatabase.getDatabase(getApplication())
    val lawListDao = database.lawListDao()

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
       remoteMediator = LawListRemoteMediator("law","law", database, apiService)
    )
    {
        lawListDao.queryAllListBeans()
    }
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()

}