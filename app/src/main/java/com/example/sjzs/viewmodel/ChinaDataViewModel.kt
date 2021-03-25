package com.example.sjzs.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.viewmodel.base.BaseDataVM
import com.example.sjzs.viewmodel.base.BaseRequestVM

class ChinaDataViewModel : BaseDataVM() {


    val listData: MutableLiveData<List<ListBean>>//列表数据
    val bannerData: MutableLiveData<List<CommomBanner>>

    init {
        listData = MutableLiveData()
        bannerData = MutableLiveData()
    }



}