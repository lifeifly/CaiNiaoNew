package com.example.banner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap

class AnimationConstriantLayout : ConstraintLayout {
    //需要裁剪的区域
    private var mCurrent: Float = 0F
    private var mRect: RectF = RectF()



    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun dispatchDraw(canvas: Canvas?) {
        canvas?.save()
        mRect.top = 0F
        mRect.bottom = measuredHeight.toFloat()
        if (mCurrent > 0) {
            mRect.left = mCurrent * measuredWidth
            mRect.right = measuredWidth.toFloat()
        } else if (mCurrent < 0) {
            mRect.left = 0F
            mRect.right = (1 + mCurrent) * measuredWidth
        } else {
            mRect.left = 0F
            mRect.right = measuredWidth.toFloat()
        }

        canvas?.clipRect(mRect)

        super.dispatchDraw(canvas)
        canvas?.restore()

    }



    /**
     * 设置进度并刷新
     */
    fun setProgress(progress: Float) {
        if (progress != mCurrent) {
            mCurrent = progress
            invalidate()
        }

    }


}