package com.example.video.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.video.R
import com.example.video.utils.ToolUtils

class ProgressContatiner : LinearLayout {
    private lateinit var mCurrentTv: TextView
    private lateinit var mTotalTv: TextView


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        mCurrentTv = findViewById(R.id.progress_current)
        mTotalTv = findViewById(R.id.progress_total)
    }


    /**
     * 总进度
     */
    fun setTotal(total: Int) {
        mTotalTv.setText(ToolUtils.makeTimeString(total))
    }

    /**
     * 显示进度
     */
    fun show(current: Int) {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
        }
        mCurrentTv.setText(ToolUtils.makeTimeString(current))
    }

    /**
     * 隐藏进度
     */
    fun hide() {
        visibility = View.GONE
    }
}