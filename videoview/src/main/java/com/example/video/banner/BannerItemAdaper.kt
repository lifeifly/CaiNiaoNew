package com.example.video.banner

import android.view.View
import kotlin.coroutines.coroutineContext

abstract class BannerItemAdaper {

    //根据位置获取itemView
    abstract fun getView(position:Int,convertView:View?):View
    //获取轮播的数量
    abstract fun getCount(): Int

}