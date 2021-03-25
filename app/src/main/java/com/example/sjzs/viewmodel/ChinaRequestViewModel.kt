package com.example.sjzs.viewmodel

import com.example.sjzs.model.DataManager
import com.example.sjzs.model.bean.CommonData
import com.example.sjzs.model.bean.beanmanager.BeanManager

import com.example.sjzs.model.observer.ResponseObserver
import com.example.sjzs.viewmodel.base.BaseRequestVM


class ChinaRequestViewModel : BaseRequestVM() {

    private var dataVM: ChinaDataViewModel? = null


    /**
     * 设置跟UI绑定的数据源
     */
    fun setDataViewModel(dataVM: ChinaDataViewModel) {
        this.dataVM = dataVM
    }


    /**
     * 请求数据
     */
    fun requestData() {
        DataManager.requestCommonData(
            "china_1.jsonp",
            "china",
            "100",
            object : ResponseObserver<CommonData>() {
                override fun onSuccess(t: CommonData) {
                    if (dataVM != null) {
                        dataVM!!.listData.value = t.list
                    }
                }

                override fun onFailure(e: Throwable) {

                }
            })
        DataManager.requestCommonBanner("china",object : ResponseObserver<String>(){
            override fun onSuccess(t: String) {
                val banners=BeanManager.getCommonBanners(t)
                dataVM!!.bannerData.value=banners
            }

            override fun onFailure(e: Throwable) {

            }
        })
    }


}