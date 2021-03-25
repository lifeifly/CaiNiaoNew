package com.example.sjzs.ui.recyclerview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView


class RvDivider : RecyclerView.ItemDecoration() {

    private val mPaint: Paint

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.strokeWidth = 3F
        mPaint.color = Color.GRAY
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        val left = parent.paddingLeft.toFloat()
        val right = parent.width - parent.paddingRight.toFloat()
        for (i in 1..childCount - 1) {
            //画分割线
            val top = parent.getChildAt(i - 1).bottom
            val bottom = parent.getChildAt(i).top
            val centerY=(top+bottom)/2.toFloat()
            c.drawLine(left,centerY,right,centerY,mPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        if (itemPosition != 0) {
            outRect.top = 5
        }
    }


}