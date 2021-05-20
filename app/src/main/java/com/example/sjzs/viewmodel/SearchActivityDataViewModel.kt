package com.example.sjzs.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.example.sjzs.model.bean.SearchBean
import com.example.sjzs.model.bean.SearchHistory
import com.example.sjzs.model.paging.SearchListDataSource
import com.example.sjzs.model.room.ListBeanDatabase
import com.example.sjzs.ui.adapter.SearchPagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivityDataViewModel(application: Application):AndroidViewModel(application) {
    //数据库
    private val database = ListBeanDatabase.getDatabase(getApplication())

    private val config = PagingConfig(20, 1, false, 60)

     val historiesData:MutableLiveData<List<SearchHistory>> = MutableLiveData()

    init {
        GlobalScope.launch(Dispatchers.Main){
            val historyDao=database.historyDao()
            val histories=historyDao.queryAllListBeans()
            historiesData.value=histories
        }
    }
    //列表数据
    var listData:LiveData<PagingData<SearchBean>>?=null


    fun search(keyword:String){
        val newKey="%"+keyword+"%"
        listData=Pager(
            config = config
        )
        {
            SearchListDataSource(newKey,database)
        }
            .flow
            .cachedIn(viewModelScope)
            .asLiveData()
    }

    /**
     * 将搜索记录插入数据库
     */
    fun insertHistory(keyword: String) {
        GlobalScope.launch(Dispatchers.IO){
            database.withTransaction {
                val dao=database.historyDao()
                dao.insertSingleBean(SearchHistory(keyword,System.currentTimeMillis()))
            }
        }
    }
}