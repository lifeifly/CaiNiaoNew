package com.example.sjzs.ui.recyclerview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class RefreshView : View {

    //画笔
    private val mPaint: Paint

    //中心点
    private lateinit var mRectF: RectF

    //半径
    private var mRadius: Float = 0F

    //固定角度
    private val ANGLE = 60F

    //变化角度
    private var changedAngle = 300F

    //开始角度
    private var startAngle = 0F

    private val animator: ValueAnimator

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mPaint = Paint()
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 8F

        animator = ObjectAnimator.ofFloat(0F, 360F)
        animator.duration = 1000
        animator.repeatCount = -1
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val current = animation.animatedValue as Float
            startAngle = current
            changedAngle = current / 360 * 300
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight > measuredWidth) {
            mRadius = measuredWidth / 2 - mPaint.strokeWidth / 2 - 8
        } else {
            mRadius = measuredHeight / 2 - mPaint.strokeWidth / 2 - 8
        }
        mRectF = RectF(
            measuredWidth / 2 - mRadius,
            measuredHeight / 2 - mRadius,
            measuredWidth / 2 + mRadius,
            measuredHeight / 2 + mRadius
        )

        val colorArray = intArrayOf(Color.RED, Color.BLUE, Color.GREEN)
        val positionArray = floatArrayOf(0.3F, 0.7F, 1.0F)
        val gradient = SweepGradient(
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 2).toFloat(),
            colorArray,
            positionArray
        )
        mPaint.setShader(gradient)
    }


    override fun onDraw(canvas: Canvas?) {
        val totalAngle = changedAngle + ANGLE
        canvas?.drawArc(mRectF, startAngle, totalAngle, false, mPaint)
    }

    /**
     * 同时旋转和加响应的旋转角度
     */
    fun start() {

        animator.start()
    }

    fun stop() {
        changedAngle=300F
        animator.cancel()
        invalidate()
    }
}