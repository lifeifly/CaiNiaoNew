package com.example.sjzs.viewmodel

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.MediaController
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.example.sjzs.model.bean.ArticleBean
import com.example.sjzs.viewmodel.base.BaseDataVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ArticleDataViewModel : BaseDataVM() {
    val data: MutableLiveData<ArticleBean>

    init {
        data = MutableLiveData()
    }

}