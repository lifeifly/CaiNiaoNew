package com.example.thumbupsample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.video.R
import java.util.zip.CheckedOutputStream

/**
 * 点赞视图的容器
 */
class ThumbContainer : LinearLayout, View.OnClickListener {
    companion object {
        private const val DEFAULT_DRAWABLE_PADDING = 4F
    }

    private lateinit var mThumbView: ThumbView
    private lateinit var mCountView: CountView

    private var mDrawablePadding = dp2Px(context, DEFAULT_DRAWABLE_PADDING)



    private var mTextColor = Color.parseColor(CountView.DEFAULT_TEXT_COLOR)
    private var mCount = 0
    private var mTextSize = sp2Px(context,CountView.DEFAULT_TEXT_SIZE)
    private var mIsThumbUp = false
    private var mTopMargin = 0
    private var mNeedChangeChildView = false


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.ThumbContainer)
        if (typeArray != null) {
            mDrawablePadding = typeArray.getDimension(
                R.styleable.ThumbContainer_tc_drawable_padding,
                mDrawablePadding
            )
            mCount = typeArray.getInt(R.styleable.ThumbContainer_tc_count, mCount)
            mIsThumbUp = typeArray.getBoolean(R.styleable.ThumbView_tv_isThumbUp, mIsThumbUp)
            mTextColor = typeArray.getColor(R.styleable.ThumbContainer_tc_text_color, mTextColor)
            mTextSize = typeArray.getDimension(R.styleable.ThumbContainer_tc_text_size, mTextSize)

            typeArray.recycle()
        }

        initView()
    }

    private fun initView() {
        removeAllViews()
        //不裁剪子view
        clipChildren = false
        orientation = HORIZONTAL

        addThumbView()

        addCountView()

        //把设置的padding分解到子view，否则对超出view范围的动画显示不全
        setPadding(0, 0, 0, 0, false)
        setOnClickListener(this)
    }

    fun setCount(count: Int): ThumbContainer {
        this.mCount = count
        mCountView.setCount(count)
        return this
    }

    fun setTextColor(textColor: Int): ThumbContainer {
        this.mTextColor = textColor
        mCountView.setTextColor(mTextColor)
        return this
    }

    fun setTextSize(textSize: Float): ThumbContainer {
        this.mTextSize = textSize
        mCountView.setTextSize(mTextSize)
        return this
    }

    fun setIsThumbUp(isThumbUp: Boolean): ThumbContainer {
        this.mIsThumbUp = isThumbUp
        mThumbView.setIsThumbUp(mIsThumbUp)
        return this
    }

    fun setThumbUpListener(listener: ThumbView.ThumbUpClickListener) {
        mThumbView.setThumbUpListener(listener)
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (mNeedChangeChildView){
            resetThumbParam()
            resetCountParams()
            mNeedChangeChildView=false
        }else{
            super.setPadding(left, top, right, bottom)
        }
    }
    private fun setPadding(l: Int, t: Int, r: Int, b: Int, needChange: Boolean) {
        this.mNeedChangeChildView = needChange
        setPadding(l, t, r, b)
    }

    private fun resetThumbParam() {
        val params = mThumbView.layoutParams as LinearLayout.LayoutParams
        if (mTopMargin < 0) {
            params.topMargin = mTopMargin
        }
        params.leftMargin = paddingLeft
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        mThumbView.layoutParams = params
    }

    private fun resetCountParams() {
        val params = mCountView.layoutParams as LinearLayout.LayoutParams
        if (mTopMargin > 0) {
            params.topMargin = mTopMargin
        }
        params.leftMargin = paddingLeft
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        params.rightMargin = paddingRight
        mCountView.layoutParams = params
    }


    private fun addCountView() {
        mCountView = CountView(context)
        mCountView.setTextColor(mTextColor)
        mCountView.setCount(mCount)
        mCountView.setTextSize(mTextSize)

        addView(mCountView, getCountParam())
    }

    private fun addThumbView() {
        mThumbView = ThumbView(context)
        mThumbView.setIsThumbUp(mIsThumbUp)
        val circlePoint = mThumbView.getCirclePoint()
        mTopMargin = ((circlePoint.getY() - mTextSize / 2).toInt())
        addView(mThumbView, getThumbParam())
    }

    private fun getThumbParam(): LayoutParams {
        val params = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //让图片于文字中心对齐
        if (mTopMargin < 0) {
            //拇指图片中心点相对于文字在上面的位置
            params.topMargin = mTopMargin
        }
        params.leftMargin = paddingLeft
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        return params
    }

    private fun getCountParam(): LayoutParams {
        val params = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        //让图片于文字中心对齐
        if (mTopMargin > 0) {
            //拇指图片中心点相对于文字在上面的位置
            params.topMargin = mTopMargin
        }
        params.leftMargin = mDrawablePadding.toInt()
        params.topMargin += paddingTop
        params.bottomMargin = paddingBottom
        params.rightMargin = paddingRight
        return params
    }

    override fun onClick(v: View?) {
        mIsThumbUp = !mIsThumbUp
        if (mIsThumbUp) {//现在是点赞状态
            mCountView.calculateChangeNum(1)
        } else {
            //取消点赞
            mCountView.calculateChangeNum(-1)
        }
        mThumbView.startAnim()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superData", super.onSaveInstanceState())
        bundle.putInt("count", mCount)
        bundle.putFloat("textSize", mTextSize)
        bundle.putInt("textColor", mTextColor)
        bundle.putBoolean("isThumbUp", mIsThumbUp)
        bundle.putFloat("drawablePadding", mDrawablePadding)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val data = state as Bundle

        super.onRestoreInstanceState(data.getParcelable("superData"))
        mCount = data.getInt("count")
        mTextSize = data.getFloat("textSize")
        mTextColor = data.getInt("textColor")
        mIsThumbUp = data.getBoolean("isThumbUp")
        mDrawablePadding = data.getFloat("drawablePadding")

        initView()
    }
}