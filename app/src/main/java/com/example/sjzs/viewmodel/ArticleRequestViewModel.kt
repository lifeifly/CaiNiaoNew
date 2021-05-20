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


class ArticleRequestViewModel(application: Application) : AndroidViewModel(application) {
    var mHostActivity:BaseActivity?=null
            set(value) {
                field = value
            }

    private var mDataVM: ArticleDataViewModel? = null

    fun setDataViewModel(dataViewModel: ArticleDataViewModel) {
        this.mDataVM = dataViewModel
    }

    fun requestData(url: String) {
        Log.d("ARTICLE", "requestData:4 ")
        getApplication<MyApplication>().iHttpProcesser.getArticle(url,object : HttpCallback<String>(){
            override fun onResult(result: String) {
                //转换成ArticleBean
                val articleBean = BeanManager.getArticleBean(result)
                mDataVM?.data?.value = articleBean
            }

            override fun onFailure(message: Throwable) {
                if (mHostActivity!=null){
                    mHostActivity?.showError("数据加载异常，点击重试")
                }
            }
        })

    }



}