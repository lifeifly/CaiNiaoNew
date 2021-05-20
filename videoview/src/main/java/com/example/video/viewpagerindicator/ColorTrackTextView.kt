package com.example.video.viewpagerindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class ColorTrackTextView : AppCompatTextView {
    //正常的画笔
    private var mNormalPaint: Paint = Paint()

    //画颜色的画笔
    private var mChangedPaint: Paint = Paint()

    //默认方向,从左往右
    private var mDirection = Direction.LEFT_TO_RIGHT

    //当前绘画的进度
    private var mCurrentProgress: Float = 0F

    //文本边框
    private val mRect: Rect
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mNormalPaint.isDither = true
        mNormalPaint.isAntiAlias = true
        mNormalPaint.textSize=25F

        mChangedPaint.isAntiAlias = true
        mChangedPaint.isDither = true
        mChangedPaint.textSize=25F

        mRect = Rect()
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        mNormalPaint.textSize=size
        mChangedPaint.textSize=size
    }

    /**
     * 设置正常画笔颜色
     */
    fun setNormalColor(color: Int) {
        mNormalPaint.color = color
    }

    /**
     * 设置改变的颜色
     */
    fun setChangeColor(color: Int) {
        mChangedPaint.color = color
    }

    /**
     * 设置当前进度，并重绘
     */
    fun setCurrentProgress(progress: Float) {
        this.mCurrentProgress = progress
        //重绘
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        //判断是否指定text
        if (TextUtils.isEmpty(text)) return
        //指定了text
        //获取中心点
        val centerY = (measuredHeight / 2).toFloat()
        val centerX = (measuredWidth / 2).toFloat()
        //获取基线
        val fontMetrics = mNormalPaint.fontMetricsInt
        val baseLine =
            ((fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent + centerY)
        //获取边框
        mNormalPaint.getTextBounds(text.toString(), 0,text.length,  mRect)
        //左侧x开始点
        val x=centerX-mRect.width()/2

        //分界点x
        val boundX=mCurrentProgress*mRect.width()

        //根据方向画
        if (mDirection==Direction.LEFT_TO_RIGHT){
            //画改变的
            //change的start和end点
            val startX=x
            val endX=startX+boundX

            drawText(canvas,x,startX,endX, baseLine,mChangedPaint)
            //画不变的
            val endNormalX=startX+mRect.width()

            drawText(canvas,x,endX,endNormalX,baseLine,mNormalPaint)
        }else if (mDirection==Direction.RIGHT_TO_LEGT){
            //画改变的,开始点从末尾处
            //change的start和end点
            val startX=x+mRect.width()-boundX
            //end不变
            val endX=x+mRect.width()

            drawText(canvas,x,startX,endX,baseLine,mChangedPaint)
            //画不变的
            //起始点不变，终点就是cahnge的起始点
            drawText(canvas,x,x,startX,baseLine,mNormalPaint)
        }
    }

    private fun drawText(canvas: Canvas?, textX:Float,start: Float,end:Float, baseLine: Float,paint: Paint) {
        //保存当前的画布
        canvas?.save()
        //裁剪
        canvas?.clipRect(start,0F,end,height.toFloat())
        canvas?.drawText(text.toString(),textX,baseLine,paint)
        //恢复
        canvas?.restore()
    }

    /**
     * 设置方向
     */
    fun setDirection(direction: Direction){
        this.mDirection=direction
    }

    /**
     * 获取方向
     */
    fun getDirection():Direction{
        return mDirection
    }

    /**
     * 方向
     */
    enum class Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEGT
    }
}