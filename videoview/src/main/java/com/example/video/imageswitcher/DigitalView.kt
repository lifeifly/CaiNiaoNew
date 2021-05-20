package com.example.video.imageswitcher

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 数字指示器
 */
class DigitalView:View {
    //画笔
    private val mChangePaint:Paint
    private val mFixedPaint:Paint
    //变化的宽度
    private var mItemWidth=0
    //颜色渐变的矩阵
    private var mGradientMatrix: Matrix?=null
    //每次移动的距离
    private var mTranslate=0
    private lateinit var mLinearGradient1:LinearGradient
    private lateinit var mLinearGradient2:LinearGradient

    private val mDefaultColors= intArrayOf(0x220000FF,0x550000FF,0x770000FF)
    private var mTotal="/0"
    private var mCurrent="1";

    private var mChangeTextWidth=0F
    private var mChangeTextSize= DEFAULT_CHANGE_TEXT_SIZE
    private var mTotalTextSize= DEFAULT_TOTAL_TEXT_SIZE
    private var mTextColor= DEFAULT_TEXT_COLOR
    //间距
    private var mSpacing=20
    companion object{
        private const val DEFAULT_CHANGE_TEXT_SIZE=45F
        private const val DEFAULT_TOTAL_TEXT_SIZE=70F//px
        private const val DEFAULT_TEXT_COLOR=0x770000FF

    }
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        mChangePaint=Paint()
        //抗锯齿
        mChangePaint.isAntiAlias=true
        //防抖动，手机越来越好，开启没啥效果
        mChangePaint.isDither=true
        mChangePaint.textSize=DEFAULT_CHANGE_TEXT_SIZE

        mFixedPaint=Paint()
        //抗锯齿
        mFixedPaint.isAntiAlias=true
        //防抖动，手机越来越好，开启没啥效果
        mFixedPaint.isDither=true
        mFixedPaint.textSize=DEFAULT_TOTAL_TEXT_SIZE
        mFixedPaint.color= DEFAULT_TEXT_COLOR
    }




    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //设置大小和文字大小相同
        mChangeTextWidth=mChangePaint.measureText(mCurrent,0,mCurrent.length)

        val totalTextWidth=mFixedPaint.measureText(mTotal,0,mTotal.length)

        setMeasuredDimension(Math.ceil((mChangeTextWidth+totalTextWidth+mSpacing).toDouble()).toInt(),
            Math.max(mTotalTextSize,mChangeTextSize).toInt()
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mItemWidth==0){
            mItemWidth=w
            if (mItemWidth>0){
                mGradientMatrix= Matrix()
                mLinearGradient1= LinearGradient(0F,0F,
                    measuredWidth.toFloat(),0F,mDefaultColors,null,Shader.TileMode.CLAMP)
                mChangePaint.setShader(mLinearGradient1)
//                mLinearGradient2= LinearGradient(0F,0F,w2.toFloat(),0F,mDefaultColors,null,Shader.TileMode.CLAMP)
//                mFixedPaint.setShader(mLinearGradient2)
            }
        }
    }

    /**
     * 设置总数
     */
    fun setTotal(total:Int){
        this.mTotal="/"+total
        requestLayout()
    }

    /**
     * 设置当前数字
     */
    fun setCurrent(current:Int){
        this.mCurrent=current.toString()
        requestLayout()
    }
    override fun onDraw(canvas: Canvas?) {
        //计算基线
        val fontMetrics=mFixedPaint.fontMetrics
        val centerY=measuredHeight/2
        val baseLine=(fontMetrics.descent-fontMetrics.ascent)/2-fontMetrics.descent+centerY
        canvas?.drawText(mCurrent,0,mCurrent.length,0F,baseLine,mChangePaint)
        canvas?.drawText(mTotal,0,mTotal.length,mChangeTextWidth,baseLine,mFixedPaint)
       if (mGradientMatrix!=null){
           mTranslate+=mItemWidth/5
           if (mTranslate>mItemWidth*2){
               mTranslate=-mItemWidth
           }
           mGradientMatrix?.setTranslate(mTranslate.toFloat(),0F)
           mLinearGradient1.setLocalMatrix(mGradientMatrix)
           postInvalidateDelayed(300)
       }

    }
}