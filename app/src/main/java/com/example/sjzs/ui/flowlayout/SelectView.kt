package com.example.sjzs.ui.flowlayout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.sjzs.R
import com.example.thumbupsample.evaluate

class SelectView : View {
    companion object {
        private val SELECT_START_COLOR = Color.parseColor("#8803A9F4")
        private val SELECT_END_COLOR = Color.parseColor("#0003A9F4")


        private val NORMAL_START_COLOR = Color.parseColor("#88888888")
        private val NORMAL_END_COLOR = Color.parseColor("#00888888")
    }

    private val mSelectBitmap: Bitmap
    private val mBitmapPaint: Paint
    private val mCirclePaint: Paint
    private var MAX_RADIUS = 0F
    private var MIN_RADIUS = 0F

    private var mCircleStrokeWidth = 3F

    private val mRipplePaint: Paint

    //是否选中
    private var isSelect = false

    //变化的radius
    private var mRadius = 0F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mSelectBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_selected)
        mBitmapPaint = Paint()
        mBitmapPaint.isAntiAlias = true

        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.strokeWidth = mCircleStrokeWidth
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.color = Color.GRAY

        mRipplePaint = Paint()
        mRipplePaint.isAntiAlias = true
        mRipplePaint.strokeWidth = 2F
        mRipplePaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val bitmapWidth = mSelectBitmap.width
        val bitmapHeight = mSelectBitmap.height
        MIN_RADIUS = (bitmapWidth / 2).toFloat()
        MAX_RADIUS = bitmapWidth * 3 / 2.toFloat()
        setMeasuredDimension(
            2*MAX_RADIUS.toInt(),
            2*MAX_RADIUS.toInt()
        )
    }


    override fun onDraw(canvas: Canvas?) {

        val centerX = measuredWidth / 2
        val centerY = measuredHeight / 2

        //选中的波纹
        val fraction = (MAX_RADIUS - mRadius) / (MAX_RADIUS - MIN_RADIUS)
        if (isSelect) {
            //画图片
            val xy=MAX_RADIUS-MIN_RADIUS
            canvas?.drawBitmap(
                mSelectBitmap,
                xy,
                xy,
                mBitmapPaint
            )

            mRipplePaint.color = evaluate(fraction, SELECT_START_COLOR, SELECT_END_COLOR)
            canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), mRadius, mRipplePaint)

        } else {
            //画外圈圆
            canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), MIN_RADIUS, mCirclePaint)
            //画波纹
            mRipplePaint.color = evaluate(fraction, NORMAL_START_COLOR, NORMAL_END_COLOR)
            canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), mRadius, mRipplePaint)
        }
    }

    /**
     * 用域属性动画画波纹
     */
    private fun setRadius(radius: Float) {
        this.mRadius = radius
        invalidate()
    }

    /**
     * 开始画波文
     */
    fun startAnim() {
        isSelect = !isSelect
        val rippleAnim = ObjectAnimator.ofFloat(this, "radius", MIN_RADIUS, MAX_RADIUS)
        rippleAnim.duration = 200
        rippleAnim.start()
        rippleAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                //动画结束后将半径置为0
                mRadius = 0F
            }
        })
    }
    fun getStatus():Boolean{
        return isSelect
    }

    fun setIsSelect(select: Boolean) {
        isSelect=select
        invalidate()
    }
}