package com.example.video.video.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.video.R

class BufferView : View {
    private val mBluePaint: Paint
    private val mRedPaint: Paint
    private val mTextPaint: Paint
    private var mCurrentProgress = 0F
    private var valueAnimator: ValueAnimator? = null

    //关联的生命周期
    //圆点的的最大半径
    private var mRadius = 0F

    //中心点
    private var mCenterX = 0F

    //起始点和终点
    private var mStartPoint = 0F
    private var mEndPoint = 0F

    //圆心直线距离
    private var mDistance = 0F

    //状态
    private var isNormal = true

    //错误信息
    private val text = "视频解析错误"

    //文本宽度也是view的宽度
    private var textWidth = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mBluePaint = Paint()
        mBluePaint.isDither = true//防抖动
        mBluePaint.isAntiAlias = true//抗锯齿
        mBluePaint.style = Paint.Style.FILL
        mBluePaint.color = context?.resources?.getColor(R.color.seekbarColor)!!

        mRedPaint = Paint()
        mRedPaint.isDither = true//防抖动
        mRedPaint.isAntiAlias = true//抗锯齿
        mRedPaint.style = Paint.Style.FILL
        mRedPaint.color = Color.YELLOW

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.isDither = true
        mTextPaint.textSize = 35F
        mTextPaint.color = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val rect = Rect()
        mTextPaint.getTextBounds(text, 0, text.length, rect)
        textWidth = rect.width()
        val width = textWidth
        val height = rect.height()
        mRadius = (height).toFloat()
        mCenterX = (width / 2).toFloat()
        mStartPoint = mCenterX - mRadius
        mEndPoint = mCenterX + mRadius
        mDistance = mEndPoint - mStartPoint
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        if (isNormal) {
            //画蓝色圆
            drawBlueCircle(canvas, mBluePaint)
            //画红色圆
            drawRedCircle(canvas, mRedPaint)
        } else {
            Log.d("TAG", "drawRedCircle: 2")

            drawErrorText(canvas, mTextPaint)
        }
    }

    private fun drawErrorText(canvas: Canvas?, textPaint: Paint) {
        val fontMetrics = textPaint.fontMetrics
        val baseLine =
            height/2 + (fontMetrics.ascent - fontMetrics.descent) / 2 + fontMetrics.descent
        val x = mCenterX - textWidth / 2
        Log.d("TAG", "drawRedCircle: $mCurrentProgress/$baseLine/$x")
        canvas?.drawText(text, 0, text.length, x, baseLine, textPaint)
    }

    private fun drawRedCircle(
        canvas: Canvas?,
        paint: Paint
    ) {

        val x: Float = mStartPoint + mCurrentProgress * mDistance

        canvas?.drawCircle(x, (height/2).toFloat(), mRadius / 2, mBluePaint)
    }

    private fun drawBlueCircle(
        canvas: Canvas?,
        paint: Paint
    ) {
        val x = mEndPoint - mCurrentProgress * mDistance
        canvas?.drawCircle(x, (height/2).toFloat(), mRadius / 2, mRedPaint)
    }

    /**
     * 开始旋转
     */
    fun start() {
        valueAnimator = ObjectAnimator.ofFloat(0F, 1F)
        valueAnimator?.duration = 700
        valueAnimator?.repeatCount = -1//循环
        valueAnimator?.addUpdateListener { animation ->
            mCurrentProgress = animation.animatedValue as Float
            invalidate()
        }
        valueAnimator?.repeatMode = ValueAnimator.REVERSE
        valueAnimator?.start()
    }

    /**
     * 停止旋转,并隐藏
     */
    fun stop() {
        if (valueAnimator!=null){
            valueAnimator?.cancel()
            valueAnimator = null
        }
        visibility = GONE
    }

    /**
     * 显示错误界面
     */
    fun showError() {
        if (valueAnimator!=null){
            valueAnimator?.cancel()
            valueAnimator = null
        }
        isNormal = false
        invalidate()
    }
}