package com.example.test

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.common.R
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.lang.AssertionError
import java.nio.Buffer


class VideoView : FrameLayout,
        SurfaceHolder.Callback,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener
        , View.OnClickListener {
    private val mMediaPlayer: MediaPlayer
    var s = "https://newcntv.qcloudcdn.com/asp/hls/1200/0303000a/3/default/fba81644dda94dc480d66bccb4fab515/1.ts"
    private val mSurfaceView: SurfaceView
    private val mCurrentProgressTv: TextView
    private val mTotalProgressTv: TextView
    private val mPlayOrPauseIv: SwitchImageView
    private val mFullScreenIv: ImageView
    private val TAG = "VIDEOVIEW"
    private lateinit var fd: File
    private var mCurrentPosition = 0
    private val mBufferView: BufferView

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    init {
        val rootView = View.inflate(context, R.layout.video_layout, this)
        mSurfaceView = rootView.findViewById(R.id.surfaceView)
        mSurfaceView.holder.addCallback(this)

        mBufferView = rootView.findViewById(R.id.buffer_view)
        mCurrentProgressTv = rootView.findViewById(R.id.current_progress)
        mTotalProgressTv = rootView.findViewById(R.id.total_progress)
        mPlayOrPauseIv = rootView.findViewById(R.id.playOrPause)
        mFullScreenIv = rootView.findViewById(R.id.full_screen)
        mFullScreenIv.setOnClickListener(this)
        mPlayOrPauseIv.setOnClickListener(this)

        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnBufferingUpdateListener(this)
        mMediaPlayer.setOnPreparedListener(this)
        mMediaPlayer.setOnCompletionListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "change: ")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "destroy: ")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun surfaceCreated(holder: SurfaceHolder) {
        mMediaPlayer.setDisplay(holder)
        Log.d(TAG, "create: " + context.getExternalFilesDir(null)?.path+File.separator+"nihao.mp4")
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        Log.d(TAG, "onBufferingUpdate: " + percent)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.d(TAG, "complete: ")
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "prepare: ")
        mMediaPlayer.start()
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
    }

    /**
     * 播放
     */
    private fun play(position: Int) {
        mMediaPlayer.reset();
        //设置需要播放的视频
        mMediaPlayer.setDataSource(fd.absolutePath);
        mMediaPlayer.prepare();
        //播放
        mMediaPlayer.start();
        mMediaPlayer.seekTo(position)
    }

    /**
     * 暂停
     */
    private fun pause() {
        if (mMediaPlayer.isPlaying) {
            //保存当前位置
            mCurrentPosition = mMediaPlayer.currentPosition
            mMediaPlayer.stop()
        }
    }


    /**
     * 全屏
     */
    private fun fullScreen() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playOrPause -> {
                mPlayOrPauseIv.switchImage()
                when (mPlayOrPauseIv.getStatus()) {
                    Status.PLAY -> {
                        play(mCurrentPosition)
                    }
                    Status.PAUSE -> {
                        pause()
                    }
                }

            }
            R.id.full_screen -> {
                fullScreen()
            }
        }
    }
}