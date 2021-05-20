package com.example.thumbupsample

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.marginTop
import com.example.video.R

/**
 * 计数试图
 */
class CountView : View {
    companion object {
        //默认文本数字的颜色
         const val DEFAULT_TEXT_COLOR = "#cccccc"

        //默认文本数字的尺寸
         const val DEFAULT_TEXT_SIZE = 15F

        //数字改变动画的执行时间
         const val COUNT_ANIMATION_DURING = 250L
    }

    //画笔
    private val mTextPaint: Paint by lazy { Paint() }

    //文本尺寸
    private var mTextSize = sp2Px(context, DEFAULT_TEXT_SIZE)

    //文本颜色
    private var mTextColor = 0

    //数字末尾颜色
    private var mEndTextColor = 0

    //数字大小
    private var mCount = 0

    //保存数字，mTexts【0】是不变的部分，mTexts【1】原来的部分，mTexts【2】变化后的部分
    private var mTexts = arrayOfNulls<String>(3)

    //各部分左标
    private var mCVPoints = arrayOfNulls<CvPoint>(3)

    //Y方向最大偏移量
    private var mMaxOffsetY = 0F

    //Y方向最小偏移量
    private var mMinOffsetY = 0F

    //原来的Y偏移量
    private var mOldOffsetY = 0F

    //现在的Y偏移量
    private var mNewOffsetY = 0F

    //动画执行的完成度
    private var mFraction = 0F

    //记录是否变大
    private var mCountToBigger = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //获取自定义属性
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.CountView)
        if (typeArray != null) {
            mCount = typeArray.getInt(R.styleable.CountView_cv_count, mCount)
            mTextColor = typeArray.getColor(
                R.styleable.CountView_cv_text_color,
                Color.parseColor(DEFAULT_TEXT_COLOR)
            )
            mTextSize = typeArray.getDimension(
                R.styleable.CountView_cv_text_size,
                mTextSize
            )
            typeArray.recycle()
        }
        init()
    }

    private fun init() {
        mCVPoints[0] = CvPoint()
        mCVPoints[1] = CvPoint()
        mCVPoints[2] = CvPoint()

        calculateChangeNum(0)

        mMinOffsetY=0F
        mMaxOffsetY=mTextSize

        mEndTextColor=Color.argb(0,Color.red(mTextColor),Color.green(mTextColor),Color.blue(mTextColor))

        mTextPaint.isAntiAlias=true
        mTextPaint.isDither=true
        mTextPaint.color=mTextColor
        mTextPaint.textSize=mTextSize

    }

    /**
     * 获取数字
     */
    fun getCount():Int{
        return mCount
    }

    /**
     * 设置数字
     */
    fun setCount(count:Int){
        this.mCount=count
        //执行数字改变的动画
        calculateChangeNum(0)
        requestLayout()
    }

    fun setTextColor(color:Int){
        this.mTextColor=color
        mEndTextColor=Color.argb(0,Color.red(mTextColor),Color.green(mTextColor),Color.blue(mTextColor))
        postInvalidate()
    }

    fun setTextSize(textSize:Float){
        this.mTextSize=textSize
        requestLayout()
    }
    //垂直位置的数字排列1234567890123456789
    fun setTextOffsetY(offset:Float){
        this.mOldOffsetY=offset//变大是【0，1】，变小是【-1，0】
        if (mCountToBigger){
            //从下到上【-1，0】
            this.mNewOffsetY=offset-mMaxOffsetY
        }else{
            //从上到下【0，1】
            this.mNewOffsetY=mMaxOffsetY+offset
        }
        mFraction=(mMaxOffsetY-Math.abs(mOldOffsetY))/(mMaxOffsetY-mMinOffsetY)
        calculatorLocation()
        postInvalidate()
    }
    fun getTextOffsetY():Float{
        return mMinOffsetY
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getOtherDefaultSize(getContentWidth()+paddingLeft+paddingRight,widthMeasureSpec),
            getOtherDefaultSize(getContentHeight()+paddingTop+paddingBottom,heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //尺寸改变重新计算各个部分的位置
        calculatorLocation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //不变的部分
        mTextPaint.color=mTextColor
        Log.d("TAG", "calculatorLocation1: "+mTexts[0]+"/"+mCVPoints[0])
        canvas?.drawText(mTexts[0]!!,mCVPoints[0]?.getX()!!,mCVPoints[0]?.getY()!!,mTextPaint)

        //变化前部分,颜色反转最后会变透明
        mTextPaint.color= evaluate(mFraction,mEndTextColor,mTextColor)
        canvas?.drawText(mTexts[1]!!,mCVPoints[1]?.getX()!!,mCVPoints[1]?.getY()!!,mTextPaint)

        //变化后的部分
        mTextPaint.color= evaluate(mFraction,mTextColor,mEndTextColor)
        canvas?.drawText(mTexts[2]!!,mCVPoints[2]?.getX()!!,mCVPoints[2]?.getY()!!,mTextPaint)
    }

    private fun getContentWidth(): Int {
        //向上取整
        return Math.ceil(mTextPaint.measureText(mCount.toString()).toDouble()).toInt()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val data=Bundle()
        data.putParcelable("superData",super.onSaveInstanceState())
        data.putInt("count",mCount)
        data.putFloat("textSize",mTextSize)
        data.putInt("textColor",mTextColor)
        return data
    }


    override fun onRestoreInstanceState(state: Parcelable?) {
        val data=state as Bundle
        val superData:Parcelable=data.getParcelable("superData")!!
        super.onRestoreInstanceState(superData)

        mCount=data.getInt("count")
        mTextColor=data.getInt("textColor")
        mTextSize=data.getFloat("textSize")

        init()
    }
    /**
     * 计算上移下移的位置
     */
    private fun calculatorLocation() {

        val text=mCount.toString()
        //每个字符的宽度
        val textWidth=mTextPaint.measureText(text)/text.length
        Log.d("TAG", "calculatorLocation: "+mTexts[0]+"/"+textWidth)
        //未改变的宽度
        val unChangeWidth=textWidth* mTexts[0]?.length!!

        val fontMetrics=mTextPaint.fontMetrics
        //基线
        val y=paddingTop+(getContentHeight()-fontMetrics.bottom-fontMetrics.top)/2

        mCVPoints[0]?.setX(paddingLeft.toFloat())
        mCVPoints[1]?.setX(paddingLeft.toFloat()+unChangeWidth)
        mCVPoints[2]?.setX(paddingLeft.toFloat()+unChangeWidth)

        mCVPoints[0]?.setY(y)
        mCVPoints[1]?.setY(y-mOldOffsetY)
        mCVPoints[2]?.setY(y-mNewOffsetY)
    }

    private fun getContentHeight(): Int {
        return mTextSize.toInt()
    }

    /**
     * 计算不变，原来和改变后的数字
     * 这里是只针对加一和减一去计算的算法，因为直接设置的时候没有动画
     */
     fun calculateChangeNum(change: Int) {
        if (change == 0) {//没有改变
            mTexts[0] = mCount.toString()
            mTexts[1] = ""
            mTexts[2] = ""
            return
        }

        val oldNum = mCount.toString()
        //相加后得到当前的数字
        val newNum = (mCount + change).toString()

        val oldArray = oldNum.toCharArray()
        val newArray = newNum.toCharArray()
        for (index in 0..oldArray.size - 1) {
            val oldChar = oldArray[index]
            val newChar = newArray[index]
            if (oldChar != newChar) {
                mTexts[0] = if (index == 0) "" else newNum.substring(0, index)
                mTexts[1] = oldNum.substring(index)
                mTexts[2] = newNum.substring(index)
                break
            }
        }
        mCount+=change
        //执行动画
        startAnim(change>0)
    }

    /**
     * 执行动画
     */
    private fun startAnim(isBigger: Boolean) {
        mCountToBigger=isBigger
        val textOffsetY=ObjectAnimator.ofFloat(this,"textOffsetY",mMinOffsetY,if (mCountToBigger) mMaxOffsetY else -mMaxOffsetY)
        textOffsetY.duration= COUNT_ANIMATION_DURING
        textOffsetY.start()
    }


}