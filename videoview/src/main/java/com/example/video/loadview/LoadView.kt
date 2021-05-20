package com.example.video.loadview

import android.animation.*
import android.content.Context
import android.content.IntentFilter
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.example.video.R

class LoadView : LinearLayout {
    //弹跳的距离
    private var tranlationDy = dp2Px(80.0F)

    //形状
    private val shapeView: ShapeView

    //阴影
    private val shadowView: View

    //标记是否停止动画
    private var isStop = false

    //标记最后一个是哪个动画，决定开启另一个动画,0为下落，1为上升
    private var mLastAnimator = 0

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //加载布局
        val root = View.inflate(context, R.layout.ui_loading, this)
        shapeView = root.findViewById<ShapeView>(R.id.shape_view)
        shadowView = root.findViewById(R.id.shadow)

    }

    private fun startFallAnimator() {
        //下落位移动画
        val fallAnimator = ObjectAnimator.ofFloat(shapeView, "translationY", 0F, tranlationDy)
        //阴影变化动画
        val scaleAnimator = ObjectAnimator.ofFloat(shadowView, "scaleX", 1F, 0.3F)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fallAnimator, scaleAnimator)
        animatorSet.duration = 500
        //重力变化加速
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.start()
        //动画执行完毕切换形状并开启上升动画
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (isStop) {
                    mLastAnimator=0
                    shapeView.clearAnimation()
                    shadowView.clearAnimation()
                } else {
                    startUpAnimator()
                }
            }
        })
    }

    /**
     * 开启上拉动画
     */
    private fun startUpAnimator() {

        //上拉动画
        val upAnimator = ObjectAnimator.ofFloat(shapeView, "translationY",  tranlationDy,0F)
        //阴影动画
        val shadowAnimator = ObjectAnimator.ofFloat(shadowView, "scaleX", 0.3F, 1.0F)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(upAnimator, shadowAnimator)
        animatorSet.duration = 500
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()
        //动画执行完毕切换形状并开启下落动画
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (isStop) {
                    shapeView.clearAnimation()
                    shadowView.clearAnimation()
                    mLastAnimator=1
                } else {
                    //切换形状
                    shapeView.invalidate()
                    //下落
                    startFallAnimator()

                    //开始旋转
                    startRotateAnimator()
                }
            }
        })
    }

    /**
     * 旋转动画
     */
    private fun startRotateAnimator() {
        val rotateAnimation: ValueAnimator
        when (shapeView.shape) {
            ShapeView.Shape.TRIANGLE -> {
                rotateAnimation = ObjectAnimator.ofFloat(shapeView, "rotation", 60F)
            }
            else -> {
                rotateAnimation = ObjectAnimator.ofFloat(shapeView, "rotation", 180F)
            }
        }
        rotateAnimation.duration = 500
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.start()
    }

    /**
     * 开始动画
     */
    fun start() {
        isStop = false
        visibility = View.VISIBLE
        if (mLastAnimator==0){
            startUpAnimator()
        }else{
            startFallAnimator()
        }
    }

    /**
     * 停止动画
     */
    fun stop() {
        isStop = true
        visibility = View.INVISIBLE
        //清理动画
        shapeView.clearAnimation()
        shadowView.clearAnimation()
    }

    /**
     * 清楚view
     */
    fun clear() {
        //把loadingview从父布局移除
        val parent = parent as ViewGroup
        parent.removeView(this)
        //移除自身所有的子View
        removeAllViews()
    }

    /**
     * dp转px
     */
    private fun dp2Px(dip: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
    }
}