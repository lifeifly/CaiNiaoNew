package com.example.video.imageswitcher

import android.view.View
import androidx.viewpager.widget.ViewPager

class SwitcherViewPagerTransform:ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.rotationY=position*50
        page.scaleX= (1-Math.abs(position)*0.3).toFloat()
        page.scaleY= (1-Math.abs(position)*0.3).toFloat()

    }
}