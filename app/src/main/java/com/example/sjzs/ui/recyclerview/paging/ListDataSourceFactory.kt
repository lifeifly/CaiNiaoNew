package com.example.sjzs.ui.recyclerview.paging

import androidx.paging.DataSource
import com.example.sjzs.model.bean.ListBean


class ListDataSourceFactory: DataSource.Factory<Int,ListBean>() {

    override fun create(): DataSource<Int, ListBean> {
        return ListDataSource()
    }
}