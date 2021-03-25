package com.example.test

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class BufferView : View {
    private val mPaint: Paint
    private var widthHeight = 0
    private var mCurrentAngle = 0F
    private val valueAnimator: ValueAnimator

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mPaint = Paint()
        mPaint.isDither = true//防抖动
        mPaint.isAntiAlias = true//抗锯齿
        mPaint.style = Paint.Style.STROKE

        valueAnimator = ObjectAnimator.ofFloat(this, "rotation", 0F, 360F)
        valueAnimator.duration = 2000
        valueAnimator.repeatCount = -1//循环
        valueAnimator.interpolator = DecelerateInterpolator()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight >= measuredWidth) {
            widthHeight = measuredWidth
        } else {
            widthHeight = measuredHeight

        }

        setMeasuredDimension(widthHeight, widthHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //设置宽度
        val strokeWidth = (widthHeight / 8).toFloat()
        mPaint.strokeWidth = strokeWidth
        //中心点
        val center = (widthHeight / 2).toFloat()
        //设置颜色渐变
        val sweepGradient = SweepGradient(center, center, Color.parseColor("#e6e6e6"), Color.parseColor("#515151"))
        mPaint.setShader(sweepGradient)

        val rectF = RectF(strokeWidth, strokeWidth, widthHeight.toFloat() - strokeWidth, widthHeight.toFloat() - strokeWidth)
        canvas?.drawArc(rectF, 0F, 295F, false, mPaint)
        start()
    }

    /**
     * 开始旋转
     */
    fun start() {
        valueAnimator.start()
    }

    /**
     * 停止旋转,并隐藏
     */
    fun stop() {
        valueAnimator.cancel()
        visibility = View.GONE
    }
}