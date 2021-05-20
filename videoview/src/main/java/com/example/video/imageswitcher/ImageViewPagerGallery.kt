package com.example.video.imageswitcher

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.banner.BannerPagerAdapter

class ImageViewPagerGallery:ViewPager {
    private lateinit var mAdapter:BannerPagerAdapter
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        setPageTransformer(true,SwitcherViewPagerTransform())
    }



    fun setAdapter(adapter:BannerPagerAdapter){
        this.mAdapter=adapter
        setAdapter(ImagePagerAdapter())
        offscreenPageLimit=mAdapter.getCount()/2+1
        pageMargin=-350
        //设置每个item的距离
        Log.d("TAG123", "setAdapter: "+pageMargin)
    }
    inner class ImagePagerAdapter:PagerAdapter(){
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view==`object`
        }

        override fun getCount(): Int {
            return mAdapter.getCount()
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view=mAdapter.getView(position,null)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
