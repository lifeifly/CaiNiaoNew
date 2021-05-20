package com.example.banner

import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager

class MyPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (position >= -1 || position <= 1) {
            //设置每个view在中间，即设置相对原位置偏移量
            page.translationX = -page.width * position
            val image = (page as AnimationConstriantLayout)
            image.setProgress(position)
        }
    }
}