package com.example.video.banner

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class BannerScroller:Scroller {
    //切换的动画持续时间
    var myScrollerDuration=850


    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, interpolator: Interpolator?) : super(context, interpolator)
    constructor(context: Context?, interpolator: Interpolator?, flywheel: Boolean) : super(context, interpolator, flywheel)

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, myScrollerDuration)

    }



}