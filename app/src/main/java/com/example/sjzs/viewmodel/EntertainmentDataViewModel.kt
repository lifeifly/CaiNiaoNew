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
import com.example.sjzs.model.paging.EnterListRemoteMediator
import com.example.sjzs.model.room.ListBeanDatabase


class EntertainmentDataViewModel(application: Application) : AndroidViewModel(application) {
    //数据库
    val database = ListBeanDatabase.getDatabase(getApplication())
    val enterListDao = database.enterListDao()

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
       remoteMediator = EnterListRemoteMediator("ent","ent", database, apiService)
    )
    {
        enterListDao.queryAllListBeans()
    }
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()

}