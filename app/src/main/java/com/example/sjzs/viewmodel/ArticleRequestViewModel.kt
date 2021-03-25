package com.example.sjzs.viewmodel

import android.util.Log
import com.example.sjzs.model.DataManager
import com.example.sjzs.model.bean.beanmanager.BeanManager
import com.example.sjzs.model.observer.ResponseObserver
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.viewmodel.base.BaseRequestVM

class ArticleRequestViewModel : BaseRequestVM() {
    private var mDataVM: ArticleDataViewModel? = null

    fun setDataViewModel(dataViewModel: ArticleDataViewModel) {
        this.mDataVM = dataViewModel
    }

    fun requestData(url: String) {
        DataManager.requestArticleData(url, object : ResponseObserver<String>() {
            override fun onSuccess(t: String) {
                //转换成ArticleBean
                val articleBean = BeanManager.getArticleBean(t)
                Log.d("ART","ARTICELBEAN->"+articleBean.videoUrl)
                mDataVM?.data?.value = articleBean
            }

            override fun onFailure(e: Throwable) {

            }
        })
    }


}