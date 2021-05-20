package com.example.sjzs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sjzs.model.bean.ArticleBean

class ArticleDataViewModel : ViewModel() {
    val data: MutableLiveData<ArticleBean>

    init {
        data = MutableLiveData()
    }

}