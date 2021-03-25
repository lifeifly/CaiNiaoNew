package com.example.sjzs.ui.activity

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.SurfaceHolder
import android.widget.MediaController
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sjzs.R
import com.example.sjzs.databinding.ActivityArticleBinding
import com.example.sjzs.model.bean.ArticleBean
import com.example.sjzs.ui.base.BaseActivity
import com.example.sjzs.viewmodel.ArticleDataViewModel
import com.example.sjzs.viewmodel.ArticleRequestViewModel
import kotlin.math.log

class ArticleActivity : BaseActivity(), MediaPlayer.OnBufferingUpdateListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
    private val dataVm: ArticleDataViewModel by lazy {
        ViewModelProvider(this).get(
            ArticleDataViewModel::class.java
        )
    }
    private val requestVm: ArticleRequestViewModel by lazy {
        ViewModelProvider(this).get(
            ArticleRequestViewModel::class.java
        )
    }

    private lateinit var binding: ActivityArticleBinding
    private var mMediaPlayer: MediaPlayer? = null
    private var mMediaController: MediaController? = null
    private var videoUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_article)
        initView()
        binding.dataVM = dataVm
        binding.setLifecycleOwner(this)
        requestVm.setDataViewModel(dataVm)
        requestData()
    }

    private fun initView() {
        binding.surfaceView.holder.addCallback(this)
        //给toolbar设置返回键
        val toolbar = binding.toolBar
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)//设置返回键可用
        //设置监听video
        dataVm.data.observe(this, object : Observer<ArticleBean> {
            override fun onChanged(t: ArticleBean?) {
                //如果videoUrl不为空，准备播放视频
                if (t?.videoUrl != null) {
                    videoUrl=t.videoUrl
                    mMediaController = MediaController(this@ArticleActivity)
                    mMediaPlayer = MediaPlayer()
                    Log.d("TAG1", "onChanged: "+t.videoUrl+"/1.ts")
                }
            }
        })


    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {
        val intent = intent
        val url = intent.getStringExtra("data")

        if (url != null) {
            requestVm.requestData(url)
        }
    }

    override fun complete() {

    }

    /**
     * 响应返回键
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        Log.d("TAG", "onBufferingUpdate: ")
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d("TAG", "onCompletion: ")
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        Log.d("TAG1", "onPrepared: ")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("TAG", "changed")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("TAG", "destroy")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (mMediaPlayer!=null){
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放声音类型
            mMediaPlayer?.setDisplay(holder)
            mMediaPlayer?.setOnPreparedListener(this)
            mMediaPlayer?.setOnBufferingUpdateListener(this)
            mMediaPlayer?.setDataSource(videoUrl+"/1.ts")
            mMediaPlayer?.prepareAsync()
        }
        Log.d("TAG", "surfaceview"+binding.surfaceView.width+binding.surfaceView.height)
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")
    }
}