package com.example.sjzs.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.sjzs.MyApplication
import com.example.sjzs.model.bean.beanmanager.BeanManager
import com.example.sjzs.model.http.hilt.HttpCallback
import com.example.sjzs.model.http.hilt.HttpHelper
import com.example.sjzs.model.observer.ResponseObserver
import com.example.sjzs.ui.base.BaseActivity


class PhotoActivityRequestViewModel(application: Application) : AndroidViewModel(application) {
    var mHostActivity:BaseActivity?=null
            set(value) {
                field = value
            }

    private var mDataVM: PhotoActivityDataViewModel? = null

    fun setDataViewModel(dataViewModel: PhotoActivityDataViewModel) {
        this.mDataVM = dataViewModel
    }

    fun requestData(url: String) {
        if (url.contains("photo")){
            getApplication<MyApplication>().iHttpProcesser.getPhoto(url,object : HttpCallback<String>(){
                override fun onResult(result: String) {
                    //转换成ArticleBean
                    val photoBean = BeanManager.getPhotoBean(result)
                    mDataVM?.data?.value = photoBean
                }

                override fun onFailure(message: Throwable) {
                    if (mHostActivity!=null){
                        mHostActivity?.showError("数据加载异常，点击重试")
                    }
                }
            })
        }else if (url.contains("military")){
            getApplication<MyApplication>().iHttpProcesser.getMilitaryPhoto(url,object : HttpCallback<String>(){
                override fun onResult(result: String) {
                    //转换成ArticleBean
                    val photoBean = BeanManager.getPhotoBean(result)
                    mDataVM?.data?.value = photoBean
                }

                override fun onFailure(message: Throwable) {
                    if (mHostActivity!=null){
                        mHostActivity?.showError("数据加载异常，点击重试")
                    }
                }
            })
        }


    }



}