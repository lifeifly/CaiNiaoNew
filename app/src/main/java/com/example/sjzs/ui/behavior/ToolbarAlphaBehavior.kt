package com.example.sjzs.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * 根据滑动的位置改变toolbar 的透明度
 */
class ToolbarAlphaBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<Toolbar>(context, attrs) {
    private val mContext = context
    //y方向总偏移量
    private var yOffset:Int=0

    //阻力系数
    private val COEFFIDENT=0.5

    //目标偏移量
    private val aimOffset=200
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Toolbar,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {

        return true
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Toolbar,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        yOffset+=dyConsumed
        if (yOffset*COEFFIDENT<=aimOffset){
            Log.d("alpha", (1-yOffset*COEFFIDENT/aimOffset).toFloat().toString())
            child.background.alpha= ((1-yOffset*COEFFIDENT/aimOffset)*255).toInt()
        }
    }

}