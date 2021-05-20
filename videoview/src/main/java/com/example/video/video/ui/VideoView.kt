package com.example.video.video.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.*
import androidx.lifecycle.LifecycleObserver
import com.example.ffmpeg07.VideoMerge
import com.example.video.R
import com.example.video.video.base.VideoControll
import com.example.video.network.DownloadManager
import com.example.video.video.utils.ToolUtils
import java.io.File
import java.lang.Exception


class VideoView : FrameLayout
    , TextureView.SurfaceTextureListener
    , MediaPlayer.OnBufferingUpdateListener
    , MediaPlayer.OnCompletionListener
    , MediaPlayer.OnPreparedListener
    , View.OnClickListener
    , VideoControll {
    private val mMediaPlayer: MediaPlayer
    private var headPath = "https://newcntv.qcloudcdn.com/asp/hls/1200/0303000a/3/default/"
    private lateinit var fd: File
    private val mBufferView: BufferView
    private val mControllContainer: ControllerContainer

    //controllcontainer的高度
    private var mContrllContainerHeight = 0

    //navigationbar的高度
    private var mNavigationHeight = 0

    //controllcontainer的显示状态
    private var mControllerStatus: ControllerStatus = ControllerStatus.HIDE

    private val mTextureView: TextureView

    //进度的容器
    private val mProgressContainer: ProgressContatiner

    //执行刷新进度的Handler
    private val EXECUTE = 0X111

    //停止进度的标记
    private val END_EXECUTE = 0x666
    private val mHandler: Handler

    //导航条
    private val mNavigationBar: LinearLayout

    //记录当前是否在操作VideoView
    @Volatile
    private var isDrag = false

    //记录Down位置
    private var downX = 0F

    //滑动的距离
    private var moveX = 0F

    //总时长
    private var mTotalTime = 0

    //快进快退基础距离
    private val BASE_RESISENT = 20

    //基准时间
    private val BASE_TIME = 1000

    //video的宽高比
    private var mWidthRatio = 0
    private var mHeightRatio = 0


    private val mRootView: View
    private var mChildVG: View? = null

    //下载的帮族类
    private var dm: DownloadManager? = null

    companion object {
        //controllcontainer距离消失的时间
        private val DISMISS_TIME = 6000L

        //动画执行时间
        private val ANIMATION_DURATION = 1000L
        private val TAG = "VIDEOVIEW"

        //当前videoview的状态,竖屏
        private val LANDSCAPE = 0x222
        private val SMALL_FLOAT = 0x333
        private val PORTRIAT = 0x444
        private var mCurrentStatus = PORTRIAT

        //需要播放的文件
        private var mVideoFile: File? = null

    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )



    init {
        mRootView = View.inflate(
            context,
            R.layout.video_layout,
            this
        )
        val mVideoViewContainer = mRootView.findViewById<ViewGroup>(R.id.video_container)
        mProgressContainer = mVideoViewContainer.findViewById(R.id.progress_container)
        mNavigationBar = mVideoViewContainer.findViewById(R.id.navigation_bar)
        val mNavigationButton = mNavigationBar.findViewById<ImageView>(R.id.navigation_iv)
        mNavigationButton.setOnClickListener(this)
        mTextureView = mVideoViewContainer.findViewById(R.id.texture_View)
        mTextureView.surfaceTextureListener = this
        mBufferView = mVideoViewContainer.findViewById(R.id.buffer_view)
        mBufferView.start()

        mControllContainer = mVideoViewContainer.findViewById(R.id.controll_container)
        mControllContainer.setControll(this)
        mTextureView.setOnClickListener(this)

        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnBufferingUpdateListener(this)
        mMediaPlayer.setOnPreparedListener(this)
        mMediaPlayer.setOnCompletionListener(this)


        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    EXECUTE -> {
                        //让控制条显示当前进度
                        val position = mMediaPlayer.currentPosition
                        mControllContainer.setCurrent(position)
                        sendEmptyMessageDelayed(EXECUTE, 1000)
                    }
                    END_EXECUTE -> {
                        //让控制条显示当前进度
                        val position = mMediaPlayer.currentPosition
                        mControllContainer.setCurrent(position)
                        //重置进度
                        mControllContainer.mCurrentProgress=0
                        removeMessages(EXECUTE)
                    }
                }
            }
        }
    }
    //监听activity的生命周期
    val mLifeCycleCallback: LifecycleObserver = object : VideoLifeCycleCallback() {
        override fun onCreate() {
            Log.d(TAG, "onCreate: ")
        }

        override fun onStart() {

        }

        override fun onResume() {

        }

        override fun onPause() {
            pause()
        }

        override fun onStop() {
        }

        override fun onDestroy() {
            if (dm != null) {
                //说明已经下载
                dm!!.stopDownloadAndMerge()
                val dir = context.getExternalFilesDir(null)
                if (dir?.exists()!!) {
                    dir.listFiles().forEach {
                        it.delete()
                    }
                }
            }
            mBufferView.stop()
            mCurrentStatus = PORTRIAT
            removeAllViews()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mContrllContainerHeight = mControllContainer.measuredHeight
        Log.d(TAG, "onMeasure: ${mNavigationBar.measuredHeight}")
        mNavigationHeight = mNavigationBar.measuredHeight
        if (mCurrentStatus == PORTRIAT) {
            val height = measuredWidth * mHeightRatio / mWidthRatio
            val parentWidthMs =
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.getMode(widthMeasureSpec))
            val parentHeightMs =
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec))
            measureChild(getChildAt(0), parentWidthMs, parentHeightMs)
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //如果视频已经播放，开启快进快退
        if (mControllContainer.visibility == View.VISIBLE && mTotalTime > 0) {
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    //记录按下的位置
                    downX = ev.x
                }
                MotionEvent.ACTION_MOVE -> {
                    moveX = ev.x - downX
                    if (moveX > 50 || moveX < -50) {
                        //移除handler消息
                        mHandler.removeMessages(EXECUTE)
                        isDrag = true
                        //计算滑动的距离,只计算横向
                        //计算对应的时间跨度
                        val timeDuration = moveX / BASE_RESISENT * BASE_TIME

                        mControllContainer.setFastForwardAndBackForward(timeDuration)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (isDrag && (moveX > 50 || moveX < -50)) {
                        //开始从转移点播放
                        mControllContainer.endOfFastForwardAndBackForward()
                        isDrag = false
                        hideProgress()
                        moveX = 0F
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 开始下载合成视频
     */
    fun downloadAndMerge(id: String, path: String) {
        val url = headPath + id
        dm = DownloadManager(url)
        try {
            dm?.downloadVideos(path, object : VideoMerge.CompleteCallback {
                override fun onComplete(targetFile: File?) {
                    //合并完毕回调,播放，取消
                    if (targetFile != null) {
                        mBufferView.stop()
                        mVideoFile = targetFile
                        start(targetFile)
                    }
                }

                override fun onError(e: Exception?) {
                    //视频解析错误
                    mBufferView.showError()
                }
            })
        } catch (e: Exception) {
            mBufferView.showError()
        }

    }

    /**
     * 开始播放
     */
    fun start(file: File) {
        //添加配置的路径和MediapPlayer
        fd = file
        play(0)
        //隐藏BufferView
        mBufferView.stop()
        //显示container
        mControllContainer.visibility = View.VISIBLE
        showControllContainer()
    }

    /**
     * 播放
     */
    override fun play(position: Int) {
        try {
            if (!mMediaPlayer.isPlaying) {
                mControllContainer.switchImage()
            }
            //改变播放键的状态
            mMediaPlayer.reset()
            //设置需要播放的视频
            mMediaPlayer.setDataSource(fd.absolutePath);
            mMediaPlayer.prepare();
            //播放
            mMediaPlayer.start();
            mMediaPlayer.seekTo(position)
        } catch (e: Exception) {
            mBufferView.showError()
        }
    }

    /**
     * 暂停
     */
    override fun pause() {
        if (mMediaPlayer.isPlaying) {
            mHandler.removeMessages(EXECUTE)
            mControllContainer.switchImage()
            mMediaPlayer.stop()
        }
    }


    /**
     * 全屏
     */
    override fun fullScreen() {
        if (mCurrentStatus == LANDSCAPE) return
        //隐藏ActionBar、状态栏，并横屏
        val activity = ToolUtils.scanForActivity(context)
        ToolUtils.hide(context)
        ToolUtils.hideStatusBar(context)
        //显示navigatin条
        mNavigationBar.visibility = View.VISIBLE
        activity?.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val screenWidth = ToolUtils.getScreenHeight(context)
        val screenHeight = ToolUtils.getScreenWidth(context)
        var width = 0
        var height = 0
        if (screenWidth * mHeightRatio / mWidthRatio < screenHeight) {
            //以width为基准
            width = screenWidth
            height = screenWidth * mHeightRatio / mWidthRatio
        } else if (screenHeight * mWidthRatio / mHeightRatio < screenWidth) {
            //以height为基准
            height = screenHeight
            width = screenHeight * mWidthRatio / mHeightRatio
        } else {
            //等比例缩放
            //宽宽比
            val ww = screenHeight * mWidthRatio / mHeightRatio / screenWidth
            //高高比
            val hh = screenWidth * mHeightRatio / mWidthRatio / screenHeight
            //以大的为准
            if (ww < hh) {
                //以高度为准
                height = screenHeight
                width = screenHeight * mWidthRatio / mHeightRatio
            } else {
                width = screenWidth
                height = screenWidth * mHeightRatio / mWidthRatio
            }
        }


        //获取decorview中的content
        val contentView =
            activity?.findViewById<ViewGroup>(android.R.id.content)
        val params =
            LayoutParams(width, height)
        params.gravity = Gravity.CENTER
        //移除rootview
        mChildVG = contentView?.getChildAt(0)
        val parentVG = ((mChildVG as ViewGroup)
            .getChildAt(1) as ViewGroup).getChildAt(0) as ViewGroup
        contentView?.removeView(mChildVG)
        contentView?.setBackgroundColor(Color.BLACK)
        parentVG.removeView(this)

        contentView?.addView(this, params)
        mCurrentStatus = LANDSCAPE
    }

    /**
     * 推出全屏
     */
    private fun exitFullScreen() {
        if (mCurrentStatus == LANDSCAPE) {
            //显示ActionBar、状态栏，并横屏
            val activity = ToolUtils.scanForActivity(context)
            ToolUtils.show(context)
            ToolUtils.showStatusBar(context)
            //隐藏navigatin条
            mNavigationBar.visibility = View.GONE
            //显示全屏图标
            mControllContainer.showFullScreenIv()
            activity?.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val screenWidth = ToolUtils.getScreenHeight(context)
            var width = screenWidth
            var height = screenWidth * mHeightRatio / mWidthRatio

            //获取decorview中的content
            val contentView =
                activity?.findViewById<ViewGroup>(android.R.id.content)
            val params =
                LayoutParams(width, height)
            params.gravity = Gravity.CENTER
            //添加rootview
            val parentVG = ((mChildVG as ViewGroup)
                .getChildAt(1) as ViewGroup).getChildAt(0) as ViewGroup
            contentView?.setBackgroundColor(Color.WHITE)
            contentView?.removeView(this)
            parentVG.addView(this, params)
            contentView?.addView(mChildVG)
            mCurrentStatus = PORTRIAT
        }
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
    }

    override fun onCompletion(mp: MediaPlayer?) {
        mControllContainer.switchImage()

        mHandler.sendEmptyMessage(END_EXECUTE)
        mControllContainer.end()
    }

    override fun onPrepared(mp: MediaPlayer?) {
        //视频总时间毫秒
        mTotalTime = mMediaPlayer.duration
        //执行进度
        mHandler.sendEmptyMessage(EXECUTE)
        mProgressContainer.setTotal(mTotalTime)
        mControllContainer.setTotal(mTotalTime)
    }

    /**
     * 隐藏中间进度
     */
    override fun hideProgress() {
        mProgressContainer.hide()
    }

    /**
     * 显示中间进度
     */
    override fun showProgress(progress: Int) {
        mProgressContainer.show(progress)
    }

    /**
     * 隐藏Controll
     */
    private fun hideControllContainer() {
        if (mControllContainer.visibility == View.GONE) return
        if (mControllerStatus == ControllerStatus.SHOW) {
            //改变状态
            mControllerStatus = ControllerStatus.HIDE
            val animator = ObjectAnimator.ofFloat(
                mControllContainer,
                "translationY",
                0F,
                mContrllContainerHeight.toFloat()
            )
            val animator1 = ObjectAnimator.ofFloat(
                mNavigationBar,
                "translationY",
                0F,
                -mNavigationHeight.toFloat()
            )
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animator, animator1)
            animatorSet.duration = 1000
            animatorSet.start()
        }
    }

    /**
     * 显示Controll
     */
    private fun showControllContainer() {
        if (mControllContainer.visibility == View.GONE) return
        if (mControllerStatus == ControllerStatus.HIDE) {
            mControllerStatus = ControllerStatus.SHOW
            val animator = ObjectAnimator.ofFloat(
                mControllContainer,
                "translationY",
                mContrllContainerHeight.toFloat(),
                0F
            )


            Log.d(TAG, "onInterceptTouchEvent:1 " + mNavigationHeight)
            val animator1 = ObjectAnimator.ofFloat(
                mNavigationBar,
                "translationY",
                -mNavigationHeight.toFloat(),
                0F
            )
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animator, animator1)

            animatorSet.duration = 1000
            animatorSet.start()
            //4秒后隐藏
            postDelayed({
                if (!isDrag) {//如果当前没有操作，4秒后隐藏
                    hideControllContainer()
                }
            }, DISMISS_TIME)
        }
    }

    /**
     * 设置拖动的状态
     */
    override fun setIsDrag(isDrag: Boolean) {
        //上次是true，这次为false，需要隐藏
        if (this.isDrag && !isDrag) {
            postDelayed({
                if (!this.isDrag) {//如果当前没有操作，4秒后隐藏
                    hideControllContainer()
                }
            }, DISMISS_TIME)
        }
        this.isDrag = isDrag
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.texture_View -> {
                if (mControllerStatus == ControllerStatus.HIDE) {//如果被隐藏，则显示
                    showControllContainer()
                }
            }
            R.id.navigation_iv -> {
                exitFullScreen()
            }
        }
    }


    fun setWidthAndHeight(widthRatio: Int, heightRatio: Int) {
        mWidthRatio = widthRatio
        mHeightRatio = heightRatio
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {

        Log.d(TAG, "onSurfaceTextureDestroyed: ")
        return false
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mMediaPlayer.setSurface(Surface(surface))
    }
}