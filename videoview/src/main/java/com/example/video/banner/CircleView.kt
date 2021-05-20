package com.example.banner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class CircleView:View {
    private val mPaint:Paint
    private val mRectF:RectF
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        mPaint= Paint()
        mPaint.isDither=true
        mPaint.isAntiAlias=true

        mRectF= RectF()
    }
    /**
     * 设置画笔颜色并重绘
     */
    fun setColor(color:Int){
        mPaint.color=color
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        mRectF.left=0F
        mRectF.top=0F
        mRectF.right=measuredWidth.toFloat()
        mRectF.bottom=measuredHeight.toFloat()
        canvas?.drawRoundRect(mRectF,5F,5F,mPaint)
    }



}