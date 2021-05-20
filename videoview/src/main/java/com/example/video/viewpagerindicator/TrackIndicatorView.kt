package com.example.video.viewpagerindicator

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.video.R
import java.lang.NullPointerException
import java.lang.RuntimeException

/**
 * ViewPager指示器
 */
class TrackIndicatorView : HorizontalScrollView  {
    //绑定的ViewPager的适配器
    private lateinit var mAdapter: IndicatorAdapter

    //指示器容器
    private val mIndicatorViewGroup: IndicatorViewGroup by lazy { IndicatorViewGroup(context) }

    //一屏显示几个
    private var visibleNum = 0

    private var mViewPager: ViewPager2? = null

    //记录当前的position
    private var mCurrentPosition = 0



    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(mIndicatorViewGroup)
        //指定Item的宽度
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView)
        if (typeArray?.getInt(R.styleable.TrackIndicatorView_tabVisibleNums, visibleNum) == null) {
            throw RuntimeException("未初始化TrackIndicatorView的visiblenum")
        } else {
            visibleNum = typeArray.getInt(R.styleable.TrackIndicatorView_tabVisibleNums, visibleNum)
        }

        typeArray.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            for (i in 0..mAdapter.getCount() - 1) {
                val view = mIndicatorViewGroup.getItemView(i)
                val params = view.layoutParams
                params.width = getItemWidth()
                params.height = measuredHeight
                view.layoutParams = params
            }
            //添加底部指示器
            mIndicatorViewGroup.addBottomIndicator(mAdapter.getBottomTrackView(), getItemWidth())
        }
    }

    /**
     * 计算每个item的宽度
     */
    private fun getItemWidth(): Int {
        //判断有没有指定个数
        val parentWidth = measuredWidth
        if (visibleNum != 0) {
            //指定了个数
            return parentWidth / visibleNum
        }
        //没有指定个数
        var itemWidth = 0
        //获取最宽的
        var maxWidth = 0
        for (i in 0..mAdapter.getCount() - 1) {
            val currentWidth = mIndicatorViewGroup.getItemView(i).measuredWidth
            maxWidth = Math.max(maxWidth, currentWidth)
        }
        itemWidth = maxWidth
        val allWidth = itemWidth * mAdapter.getCount()
        //判断是否大于一个屏幕的宽度
        if (allWidth < parentWidth) {
            //小于一屏，直接宽度/个数
            itemWidth = parentWidth / mAdapter.getCount()
        }
        //大于一屏用最大的
        return itemWidth
    }

    fun setAdapter(adapter: IndicatorAdapter?, viewPager: ViewPager2) {
        if (adapter == null) {
            throw NullPointerException("adapter is null")
        }
        this.mAdapter = adapter
        //指示器居中
        mViewPager = viewPager

        //添加监听器
        mViewPager?.registerOnPageChangeCallback(PageChangeCallback())
        setAdapter(adapter)
    }

    /**
     * 设置绑定的适配器,并添加到容器中
     */
    fun setAdapter(adapter: IndicatorAdapter) {

        val itemCount = adapter.getCount()
        //循环添加view
        for (i in 0..itemCount - 1) {
            val view = adapter.getView(i, mIndicatorViewGroup)
            mIndicatorViewGroup.addIndicatorItem(view)

            if (mViewPager != null) {
                switchItem(view, i)
            }
        }
        //默认点亮第一个
        mAdapter.lightIndicator(mIndicatorViewGroup.getItemView(0))
    }

    //设置点击事件随viewpager联动
    private fun switchItem(view: View, position: Int) {
        view.setOnClickListener { v ->
            mViewPager?.setCurrentItem(position)
        }
    }
    inner class PageChangeCallback:ViewPager2.OnPageChangeCallback(){
        override fun onPageScrollStateChanged(state: Int) {
            if (state==0){
                for (i in 0..mAdapter.getCount()-1){
                    if (i!=mCurrentPosition){
                        mAdapter.resetIndicator(mIndicatorViewGroup.getItemView(i))
                    }
                }
            }
            Log.d("INDICATOR", "onPageScrollStateChanged:3 "+state)
        }

        /**
         * 滚动的时候调用
         */
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            //滚到屏幕中间
            scrollCurrentIndicator(position, positionOffset)
            //滚动底部的指示器
            mIndicatorViewGroup.scrollBottomIndicator(position, positionOffset)
            //文本随位置变化
            mIndicatorViewGroup.scrollHeaderTextIndicator(position, positionOffset)
            Log.d("INDICATOR", "onPageScrolled: " + position + "/" + positionOffset)
        }

        override fun onPageSelected(position: Int) {
            //将当前位置赋值给mCurrentPosition
            mCurrentPosition = position
        }

        /**
         * 滚动当前的上部指示器
         */
        private fun scrollCurrentIndicator(position: Int, positionOffset: Float) {
            val totalScrollDistance = (position + positionOffset) * getItemWidth()
            //实现中间位置，一个item在中间，左边的对应剩余的距离
            val offsetScroll = (width - getItemWidth()) / 2
            //总偏移量减去在中间时左边的距离，就是scrollview应该滑动的距离，可以实现选中的indicator在屏幕中间
            val finalScroll = totalScrollDistance - offsetScroll
            //移到
            scrollTo(finalScroll.toInt(), 0)
        }

    }



}