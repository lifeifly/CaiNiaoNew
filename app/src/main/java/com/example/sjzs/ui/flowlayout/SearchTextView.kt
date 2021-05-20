package com.example.sjzs.ui.flowlayout

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.example.sjzs.R


class SearchTextView : View {
    //底层画笔
    private val mBottomPaint: Paint

    //高层画笔
    private val mTopPaint: Paint

    //颜色
    private var mColor = Color.GRAY

    //形状0圆角1矩形
    private var mShape = 0

    //文本尺寸
    private var mSize = 40F

    //圆角半径
    private var mRadius = 30F

    //源文本
    private var mSourceText = ""

    //现文本
    private var mCurText = ""
    private var mRect = Rect()


    //是否显示删除按钮
    private var isShowDelete = false


    private var mRectF = RectF()

    //x起点和基线
    private var mStart = 0F
    private var baseLine = 0F

    //文字宽高
    private var mTextHeihgt = 0F
    private var mTextWidth = 0F

    //最大字数
    private val MAX_SIZE = 10

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.SearchView)
        if (a != null) {
            mColor = a.getColor(R.styleable.SearchTextView_colorText, mColor)
            mShape = a.getInt(R.styleable.SearchTextView_shape, mShape)
            mSize = a.getDimension(R.styleable.SearchTextView_sizeText, mSize)
            mRadius = a.getDimension(R.styleable.SearchTextView_radiusCorner, mRadius)
        }
        a?.recycle()
        mBottomPaint = Paint()

        mBottomPaint.textSize = mSize
        mBottomPaint.isDither = true
        mBottomPaint.isAntiAlias = true
        mTopPaint = Paint()

        mTopPaint.textSize = mSize
        mTopPaint.isDither = true
        mTopPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取文本的大小
        if (TextUtils.isEmpty(mSourceText)) {
            setMeasuredDimension(0, 0)
            return
        }


        //如果超过字数限制，就去除后面的字+。。
        if (mSourceText.length > MAX_SIZE) {
            mCurText = mSourceText.substring(0, 5) + ".."
        } else {
            mCurText = mSourceText
        }
        //测量现文本宽高
        mBottomPaint.getTextBounds(mCurText, 0, mCurText.length, mRect)
        mTextWidth= mRect.width().toFloat()
        mTextHeihgt=mRect.height().toFloat()
        val mWidth = mRect.width() + 2 * mRadius
        val mHeight = mRect.height() + mRadius
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        if (TextUtils.isEmpty(mSourceText)) return
        mBottomPaint.color = mColor
        mRectF.left = 0F
        mRectF.right = measuredWidth.toFloat()
        mRectF.top = 0F
        mRectF.bottom = measuredHeight.toFloat()
        //圆角半径
        val radius = measuredHeight / 2.toFloat()
        //画圆角矩形
        canvas?.drawRoundRect(mRectF, radius, radius, mBottomPaint)
        //画文字
        val fontMetrics = mBottomPaint.fontMetrics
        //计算基线
        mStart = (measuredWidth - mTextWidth) / 2
        baseLine = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent + measuredHeight / 2
        mBottomPaint.color = Color.BLACK
        //写文本
        canvas?.drawText(mCurText, 0, mCurText.length, mStart, baseLine, mBottomPaint)

        //判断是否显示删除按钮
        if (isShowDelete) {
            mTopPaint.color = Color.WHITE
            canvas?.drawCircle(measuredWidth - mRadius, (measuredHeight / 2).toFloat(), measuredHeight / 2 * 0.6F, mTopPaint)
            mTopPaint.getTextBounds("X", 0, 1, mRect)
            mTopPaint.color = Color.BLACK
            canvas?.drawText("X", 0, 1, measuredWidth - mRadius - mRect.width() / 2, baseLine, mTopPaint)
        }
    }

    fun setText(text: String) {
        this.mSourceText = text
        invalidate()
    }

    fun setShowDelete(isShow: Boolean) {
        this.isShowDelete = isShow
        invalidate()
    }

    fun getIsShow(): Boolean {
        return isShowDelete
    }

    fun getText(): String {
        return mSourceText
    }

}