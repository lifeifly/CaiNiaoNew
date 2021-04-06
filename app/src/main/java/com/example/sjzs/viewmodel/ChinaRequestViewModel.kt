package com.example.sjzs.viewmodel

import android.util.Log
import com.example.sjzs.model.DataManager
import com.example.sjzs.model.bean.CommonData
import com.example.sjzs.model.bean.beanmanager.BeanManager
import com.example.sjzs.model.observer.ResponseObserver
import com.example.sjzs.ui.activity.ArticleActivity
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
        DataManager.requestCommonBanner("china", object : ResponseObserver<String>() {
            override fun onSuccess(t: String) {
                val banners = BeanManager.getCommonBanners(t)
                dataVM!!.bannerData.value = banners
            }

            override fun onFailure(e: Throwable) {

            }
        })
    }


}