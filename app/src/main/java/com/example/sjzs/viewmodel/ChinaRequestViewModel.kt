package com.example.sjzs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.sjzs.MyApplication
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.CommomBanner
import com.example.sjzs.model.bean.beanmanager.BeanManager
import com.example.sjzs.model.http.hilt.HttpCallback

class ChinaRequestViewModel(application: Application) : AndroidViewModel(application) {
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
        if (Utils.isNetworkConnected(getApplication())) {
            getApplication<MyApplication>().iHttpProcesser.getChinaBanner("china",object : HttpCallback<String>(){
                override fun onResult(result: String) {
                    if (dataVM != null) {
                        val banners = BeanManager.getCommonBanners(result)
                        dataVM!!.bannerData.value = banners
                    }
                }

                override fun onFailure(message: Throwable) {
                    Toast.makeText(getApplication(), "广告获取失败", Toast.LENGTH_SHORT).show()
                }
            })


        } else {
            val failureBanner= mutableListOf(CommomBanner("","","加载失败",""))
            dataVM?.bannerData?.value=failureBanner
            Toast.makeText(getApplication(), "当前网络异常,广告刷新失败", Toast.LENGTH_SHORT).show()
        }
    }


}