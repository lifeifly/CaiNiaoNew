package com.example.banner

import android.view.View

abstract class BannerPagerAdapter {

    abstract fun getView(position:Int,convertView:View?): View
    abstract fun getCount():Int
}