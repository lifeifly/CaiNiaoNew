package com.example.sjzs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.sjzs.MyApplication
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.PhotoList
import com.example.sjzs.model.http.hilt.HttpCallback

class PictureRequestViewModel(application: Application) : AndroidViewModel(application) {
    private var dataVM: PictureDataViewModel? = null

    /**
     * 设置跟UI绑定的数据源
     */
    fun setDataViewModel(dataVM: PictureDataViewModel) {
        this.dataVM = dataVM
    }

    /**
     * 请求数据
     */
    fun requestData() {
        if (Utils.isNetworkConnected(getApplication())) {
            getApplication<MyApplication>().iHttpProcesser.getPhotoBanner("index.json",object : HttpCallback<PhotoList>(){
                override fun onResult(result: PhotoList) {
                    if (dataVM != null) {
                        dataVM?.bannerList?.value=result.rollData.subList(result.rollData.size-4,result.rollData.size)
                    }
                }

                override fun onFailure(message: Throwable) {
                    Toast.makeText(getApplication(), "广告获取失败", Toast.LENGTH_SHORT).show()
                }
            })


        } else {
            Toast.makeText(getApplication(), "当前网络异常,广告刷新失败", Toast.LENGTH_SHORT).show()
        }
    }


}