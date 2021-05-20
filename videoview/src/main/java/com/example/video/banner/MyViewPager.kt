package com.example.banner

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


/**
 * 实现层叠效果
 */
class MyViewPager : ViewPager {
    private lateinit var mAdapter: BannerPagerAdapter
    private var activityCallback: ActivityLifecycleCallback? = null


    companion object{
        val SCROLL_SIGNAL = 0X0011
        val SCROLL_DELAY_TIME = 5000L
    }

    private var mHandler = ScrollHandler(this)


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setPageTransformer(true, MyPageTransformer())
    }

    fun startRoll() {
        mHandler.removeMessages(SCROLL_SIGNAL)
        mHandler.sendEmptyMessageDelayed(SCROLL_SIGNAL, SCROLL_DELAY_TIME)
    }

    fun setAdapter(adapter: BannerPagerAdapter) {
        this.mAdapter = adapter
        setAdapter(AdapterWrapper(mAdapter))
        if (mAdapter.getCount() > 1) {
            startRoll()
        }
        activityCallback = ActivityLifecycleCallback(context,mHandler,adapter)
        (context as Activity).application.registerActivityLifecycleCallbacks(
            activityCallback
        )
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(SCROLL_SIGNAL)
        if (activityCallback != null) {
            (context as Activity).application.unregisterActivityLifecycleCallbacks(
                activityCallback
            )
            activityCallback = null
        }
    }

    class AdapterWrapper(private val adapter: BannerPagerAdapter) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return Int.MAX_VALUE
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = adapter.getView(position % adapter.getCount(), null)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    class ScrollHandler(private val myViewPager: MyViewPager):Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            if (msg.what == SCROLL_SIGNAL) {
                //每隔几秒切换
                myViewPager.setCurrentItem(myViewPager.currentItem + 1)
                myViewPager.startRoll()
            }
        }
    }

     class ActivityLifecycleCallback(private val context: Context,private val handler: Handler,private val adapter:BannerPagerAdapter) : ActivityCallback() {
        override fun onActivityPaused(activity: Activity) {
            super.onActivityPaused(activity)
            //判断是不是监听的当前activity的生命周期，因为会回调所有activity的生命周期
            if (activity == context) {
                handler.removeMessages(SCROLL_SIGNAL)
            }
        }

        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            //开启轮播
            if (activity == context) {
                if (adapter.getCount() > 1) {
                    handler.sendEmptyMessageDelayed(SCROLL_SIGNAL, SCROLL_DELAY_TIME)
                }
            }
        }
    }
}
