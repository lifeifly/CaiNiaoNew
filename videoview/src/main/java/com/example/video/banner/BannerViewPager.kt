package com.example.video.banner

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager



class BannerViewPager : ViewPager {
    private lateinit var mAdaper: BannerItemAdaper
    private val SCROLL_WAHT = 0X111
    private val SCROLL_DELAY_TIME = 3000L
    private val mScroller: BannerScroller

    //服用View
    private  val mConverView: ArrayList<View>

    //会造成内存泄漏
    private var mHandler: Handler? = Handler(Handler.Callback {
        if (it.what == SCROLL_WAHT) {
            //每隔几秒后切换
            setCurrentItem(currentItem + 1)
            startRoll()
        }
        return@Callback true
    })

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //改变切换页面的速率
        //源码只能改变mScroller.startScroll（duration）中的duration
        //通过反射获取Sroller
        val field = ViewPager::class.java.getDeclaredField("mScroller")
        //设置参数
        /**
         * 第一个参数pbject：当前属性在哪个类
         * 第二个参数value：值
         */
        mScroller = BannerScroller(context)
        //private加上这行菜吗才能访问
        field.isAccessible = true
        field.set(this, mScroller)

        mConverView = ArrayList<View>()

    }

    fun setAdapter(adapter: BannerItemAdaper) {
        mAdaper = adapter
        super.setAdapter(BannerPagerAdapter())
        //关联Activity的声明周期
        (context as Activity).application.registerActivityLifecycleCallbacks(
            mActivityLifecycleCallBack
        )
    }

    /**
     * 设置自动轮播
     */

    fun startRoll() {
        //先清楚消息，防止多人同时调用
        mHandler?.removeMessages(SCROLL_WAHT)
        mHandler?.sendEmptyMessageDelayed(SCROLL_WAHT, SCROLL_DELAY_TIME)

    }

    //解决ahndler造成的内存泄露
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler?.removeMessages(SCROLL_WAHT)
        mHandler = null

        //解除生命周期的绑定
        (context as Activity).application.unregisterActivityLifecycleCallbacks(
            mActivityLifecycleCallBack
        )
    }

    fun setScrollerDuration(scrollDuration: Int) {
        mScroller.myScrollerDuration = scrollDuration
    }

    inner class BannerPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            //为了实现无限循环
            return Int.MAX_VALUE
        }

        //创建Viewpager条目的方法
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            //采用adapter设计模式
            val itemView = mAdaper.getView(position % mAdaper.getCount(), getConvertView())
            //添加到ViewPAger中
            container.addView(itemView)
            return itemView
        }

        //销毁条目的方法
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
//            mConverView=`object`
            mConverView.add(`object`)
        }

    }

    /**
     * 获取服用的界面
     */
    private fun getConvertView(): View? {
        for (i in 0..(mConverView.size) - 1) {
            //说明没有被添加给ViewPager
            if ((mConverView.get(i)).parent == null) {
                return mConverView.get(i)
            }
        }
        return null
    }


    val mActivityLifecycleCallBack = object : ActivityCallBack() {
        override fun onActivityPaused(activity: Activity) {
            super.onActivityPaused(activity)
            //判断是不是监听的当前Activity的声明周期，因为会回调所有Activty的声明周期
            if (activity == context) {
                //推出前台不可见，关闭轮播
                mHandler?.removeMessages(SCROLL_WAHT)
            }

        }

        //获取焦点，开启轮播
        override fun onActivityResumed(activity: Activity) {
            super.onActivityResumed(activity)
            if (activity == context) {
                mHandler?.sendEmptyMessageDelayed(SCROLL_WAHT, SCROLL_DELAY_TIME)
            }
        }
    }
}