package com.example.video.viewpagerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout

/**
 * 可伸长压缩的底部指示器
 */
class ElongationCompressionView : View {
    //长度
    private var mWidth = 0

    //宽度
    private var mHeight = 0

    //基准偏移量
    private var mStandardLeftMargin = 0

    //一个item的长度
    private var mItemWidth = 0

    //上次的位置
    private var mLastPosition = -1


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setWidthHeight(width: Int, height: Int, itemWidth: Int) {
        this.mWidth = width
        this.mHeight = height
        this.mItemWidth = itemWidth
        this.mStandardLeftMargin = (itemWidth - mWidth) / 2
        strechWidth(0)
    }


    /**
     * 设置动态的延申和margin
     */
    fun setStretchMargin(position: Int, positionOffset: Float) {
        if (positionOffset == 0F) {//到达终点还原长度和宽度
            resetWidthMargin(position, positionOffset)
            //将当前位置赋予上次的位置
            mLastPosition = position
            return
        }
        //非整偏移量
        val leftPositionOffset = positionOffset * mItemWidth
        //延伸长度和margin
        if (position >= mLastPosition) {
            //右滑,leftPositionOffset越来越大
            //不超过mItemWidth的三分之一，用来增加长度，超过的部分增加margin
            if (leftPositionOffset < mItemWidth / 3) {
                //增加长度
                strechWidth(leftPositionOffset.toInt())
            } else if (leftPositionOffset <= mItemWidth) {
                //增加margin
                setLeftMargin((leftPositionOffset-mItemWidth/3).toInt())
            }
        } else {
            //左滑，leftPositionOffset越来越小
            //超过mItemWidth的三分之二，用来增加长度，并减少margin，其余的部分减少margin
            //超过2*mItemWidth/3的部分增加长度和margin
            val a=(mItemWidth-leftPositionOffset).toInt()
            if (leftPositionOffset>2*mItemWidth/3){
                strechWidth(a)
            }
            //减少margin
            setLeftMargin(-a)
        }
    }


    /**
     * 延申长度
     */
    fun strechWidth(offset: Int) {
        if (layoutParams != null) {
            val params = layoutParams
            params.width = offset + mWidth
            layoutParams=params
        }
    }


    /**
     * 设置偏移
     */
    fun setLeftMargin(leftMargin: Int) {
        if (layoutParams != null) {
            //根据上次的位置计算对应位置的margin
            val lastMargin=mLastPosition*mItemWidth+mStandardLeftMargin
            //加上对应的margin
            val params=layoutParams as FrameLayout.LayoutParams
            params.leftMargin=lastMargin+leftMargin
            layoutParams=params
        }
    }

    /**
     * 恢复长度和margin
     */
    fun resetWidthMargin(position: Int, positionOffset: Float) {
        if (layoutParams != null&&layoutParams is FrameLayout.LayoutParams) {
            val totalOffset = (position + positionOffset) * mItemWidth
            //当前长度
            val width = layoutParams.width
            //获取需要减去的长度
            val changeWidth = Math.abs(width - mWidth)
            //当前margin
            val margin = (layoutParams as FrameLayout.LayoutParams).leftMargin

            //获取需要增加的margin
            val changeMargin = Math.abs(totalOffset + mStandardLeftMargin - margin)

            if (position > mLastPosition) {
                //右滑
                //开启动画
                val widthAnimator = ValueAnimator.ofInt(changeWidth)
                widthAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val params = layoutParams
                    params.width = width - value
                    layoutParams = params
                }
                Log.d("TAG123412", "resetWidthMargin:2 "+margin+"/"+changeMargin)
                val marginAnimator = ValueAnimator.ofInt(changeMargin.toInt())
                marginAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val params = layoutParams
                    (params as FrameLayout.LayoutParams).leftMargin = margin + value
                    layoutParams = params
                }
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(widthAnimator, marginAnimator)
                animatorSet.duration = 100
                animatorSet.interpolator = BounceInterpolator()
                animatorSet.start()
            } else {
                //左滑
                //开启动画
                val widthAnimator = ValueAnimator.ofInt(changeWidth)
                widthAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val params = layoutParams
                    params.width = width - value
                    layoutParams = params
                }
                Log.d("TAG123412", "resetWidthMargin:1 ")
                val marginAnimator = ValueAnimator.ofInt(changeMargin.toInt())
                marginAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val params = layoutParams
                    (params as FrameLayout.LayoutParams).leftMargin = margin - value
                    layoutParams = params
                }
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(widthAnimator, marginAnimator)
                animatorSet.duration = 100
                animatorSet.interpolator = BounceInterpolator()
                animatorSet.start()
            }
        }
    }


}