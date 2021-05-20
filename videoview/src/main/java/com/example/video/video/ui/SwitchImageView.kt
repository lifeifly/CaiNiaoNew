package com.example.video.video.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import com.example.video.R

class SwitchImageView : AppCompatImageView {
    //需要切换的暂停图片
    private val mPauseDrawable: Drawable
    //默认的src图片,播放图片
    private val mPlayDrawable:Drawable
    //当前状态默认为pause
    private var status= VideoStatus.PAUSE

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typeArray = context?.obtainStyledAttributes(attrs,
            R.styleable.SwitchImageView
        )
        mPlayDrawable=drawable
        mPauseDrawable = typeArray?.getDrawable(R.styleable.SwitchImageView_switchDrawable)!!
    }

    fun switchImage(){
        when(status){
            VideoStatus.PAUSE ->{
                setImageDrawable(mPauseDrawable)
                status= VideoStatus.PLAY
                Log.d("STATUS1",status.name)
            }
            VideoStatus.PLAY ->{
                setImageDrawable(mPlayDrawable)
                status= VideoStatus.PAUSE
                Log.d("STATUS2",status.name)
            }
        }
    }

    fun getStatus(): VideoStatus {
        return status
    }

}