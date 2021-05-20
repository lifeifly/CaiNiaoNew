package com.example.video.loadview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import java.time.chrono.HijrahEra

class ShapeView : View {
    //当前形状
    var shape: Shape = Shape.CIRCLE

    //画笔
    private val mPaint: Paint

    //高度宽度
    private var mWidth = 0
    private var mHeight = 0
    //画三角形的path
    private val mPath= Path()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        mPaint= Paint()
        mPaint.isAntiAlias=true
        mPaint.isDither=true

    }

    /**
     * 获取宽高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth=MeasureSpec.getSize(widthMeasureSpec)
        mHeight=MeasureSpec.getSize(heightMeasureSpec)
        //保证是正方形，以小的为准
        setMeasuredDimension(Math.min(mWidth,mHeight),Math.min(mWidth,mHeight))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center=width/2
        when(shape){
            Shape.CIRCLE->{
                mPaint.color= Color.RED
                canvas?.drawCircle(center.toFloat(),center.toFloat(),center.toFloat(),mPaint)
                //指定下次的形状
                shape=Shape.TRIANGLE
            }
            Shape.SQUARE->{
                mPaint.color=Color.GREEN
                canvas?.drawRect(0F,0F,width.toFloat(),height.toFloat(),mPaint)
                //指定下次形状
                shape=Shape.CIRCLE
            }
            Shape.TRIANGLE->{
                //等腰三角形需要计算另外两个点
                //两侧y点
                val y=Math.sqrt(Math.pow(width.toDouble(),2.0)-Math.pow(width/2.toDouble(),2.0))
                mPaint.color=Color.YELLOW

                mPath.reset()
                mPath.moveTo(width/2.toFloat(),0F)
                mPath.lineTo(0F,y.toFloat())
                mPath.lineTo(width.toFloat(),y.toFloat())
                mPath.close()

                canvas?.drawPath(mPath,mPaint)
                shape=Shape.SQUARE
            }
        }
    }
    enum class Shape {
        //形状
        CIRCLE, SQUARE, TRIANGLE
    }
}