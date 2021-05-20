package com.example.video.video.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.video.R
import com.example.video.video.base.VideoControll
import com.example.video.video.utils.ToolUtils

class ControllerContainer : LinearLayout, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private var mVideoControll: VideoControll? = null
    private lateinit var mCurrentProgressTv: TextView
    private lateinit var mTotalProgressTv: TextView
    private lateinit var mPlayOrPauseIv: SwitchImageView
    private lateinit var mFullScreenIv: ImageView

    //进度条
    private lateinit var mSeekBar: SeekBar

    //当前是否在拖拽seekbar
    private var isDrag = false

    //快进快退前的进度
    private var mBeforeBackAndForwardProgress = 0


    //当前进度
    var mCurrentProgress: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        mCurrentProgressTv = findViewById(R.id.current_progress)
        mTotalProgressTv = findViewById(R.id.total_progress)
        mPlayOrPauseIv = findViewById(R.id.playOrPause)
        mFullScreenIv = findViewById(R.id.full_screen)
        mSeekBar = findViewById(R.id.seekBar)
        mSeekBar.setOnSeekBarChangeListener(this)
        mFullScreenIv.setOnClickListener(this)
        mPlayOrPauseIv.setOnClickListener(this)
    }

    fun setControll(videoControll: VideoControll) {
        this.mVideoControll = videoControll
    }

    /**
     * 改变播放暂停图片
     */
    fun switchImage() {
        mPlayOrPauseIv.switchImage()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playOrPause -> {
                when (mPlayOrPauseIv.getStatus()) {
                    VideoStatus.PLAY -> {
                        mVideoControll?.pause()
                    }
                    VideoStatus.PAUSE -> {
                        Log.d("Pause", "onStopTrackingTouch: " + mCurrentProgress)
                        mVideoControll?.play(mCurrentProgress)
                    }
                }
            }
            R.id.full_screen -> {
                //设置全屏
                mVideoControll?.fullScreen()
                mFullScreenIv.visibility = View.GONE
            }
        }
    }

    /**
     * 设置总时长
     */
    fun setTotal(total: Int) {
        mTotalProgressTv.setText(ToolUtils.makeTimeString(total))
        mSeekBar.max = total
    }

    /**
     * seekbar的监听方法
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        mCurrentProgress = progress
        //进度更改方法
        //如果当前在拖拽,显示中间的进度
        if (isDrag) {
            mVideoControll?.showProgress(progress)
        } else {
            mBeforeBackAndForwardProgress = progress
        }
        mCurrentProgressTv.setText(ToolUtils.makeTimeString(progress))
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //开始一个触摸手势
        isDrag = true
        mVideoControll?.setIsDrag(true)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        isDrag = false
        //隐藏中间进度
        mVideoControll?.hideProgress()
        //开始播放
        mVideoControll?.play(mCurrentProgress)
        mVideoControll?.setIsDrag(false)
    }

    /**
     * 显示全屏图标
     */
    fun showFullScreenIv() {
        mFullScreenIv.visibility = View.VISIBLE
    }

    /**
     * 设置当前进度
     */
    fun setCurrent(currentPosition: Int) {
        mBeforeBackAndForwardProgress = currentPosition
        mSeekBar.progress = currentPosition
    }

    /**
     * 设置快进快退
     */
    fun setFastForwardAndBackForward(timeDuration: Float) {
        isDrag = true
        //设置seekbar进度为快进快退前的时间加上时间间隔
        var realProgress = timeDuration + mBeforeBackAndForwardProgress
        if (realProgress <= 0F) {
            realProgress = 0F
        }
        //防止多次seekbar进度不改变，progress不消失
        mSeekBar.progress = (realProgress).toInt()
    }

    /**
     * 快进快退结束
     */
    fun endOfFastForwardAndBackForward() {
        isDrag = false
        mCurrentProgress = mSeekBar.progress
        mVideoControll?.play(mCurrentProgress)
    }

    /**
     * 结束后初始化
     */
    fun end() {
        mCurrentProgress = 0
        mBeforeBackAndForwardProgress = 0
        isDrag = false
    }
}