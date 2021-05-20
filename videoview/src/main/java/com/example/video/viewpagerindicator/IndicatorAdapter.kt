package com.example.video.viewpagerindicator

import android.view.View
import android.view.ViewGroup

abstract class IndicatorAdapter {

    //获取总共的个数
    abstract fun getCount():Int
    //获取对应位置的view
    abstract fun getView(position:Int,parent:ViewGroup):View

    //点亮当前位置
    open fun lightIndicator(view: View){}
    //重置当前位置
    open fun resetIndicator(view: View){}
    //添加底部指示器
    abstract fun getBottomTrackView():View

}