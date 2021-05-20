package com.example.sjzs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sjzs.model.bean.ArticleBean
import com.example.video.imageswitcher.PhotoBean

class PhotoActivityDataViewModel : ViewModel() {
    val data: MutableLiveData<PhotoBean>

    init {
        data = MutableLiveData()
    }

}