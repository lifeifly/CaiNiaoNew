package com.example.sjzs.ui.scrollview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.banner.BannerView
import com.example.sjzs.R

class PagingNestedScrollView : NestedScrollView {
    private lateinit var mBannerView: BannerView
    private lateinit var mRecyclerView: RecyclerView

    //惯性滑动帮助类
    private val mFlingHelper:FlingHelper
    private var velocityY: Int = 0
    var isStartFling: Boolean = false
    private var totalDy = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mFlingHelper= FlingHelper(context)
        setOnScrollChangeListener(object : OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (isStartFling){
                    totalDy=0
                    isStartFling=false
                }
                if (scrollY==0){
                    //refreshLayout.setEnabled(true)
                }
                if (scrollY==(getChildAt(0).measuredHeight)- v?.measuredHeight!!){
                    dispatchChildFling()
                }
                //在RECYCLERVIEW FLING的情况下，记录当前的recyclerview在y轴的偏移
                totalDy+=scrollY-oldScrollY
            }
        })
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mBannerView = getChildAt(0).findViewById(R.id.banner_view)
        mRecyclerView = getChildAt(0).findViewById(R.id.recycler_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //调整recyclerview的高度为父容器高度
        val params = mRecyclerView.layoutParams
        params.height = measuredHeight
        mRecyclerView.layoutParams = params
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type)
        //向上滑动，若当前topview，可见，需要将topview滑动至不可见
        val isNeedHideTopView = (dy > 0 && scrollY < mBannerView.measuredHeight)
        if (isNeedHideTopView) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    /**
     * 分发孩子的惯性滑动
     */
    private fun dispatchChildFling() {
        if (velocityY != 0) {
            //说明有惯性,转化成距离减去自己滑的距离就是孩子的距离
            val splingFlingDistance = mFlingHelper.getSplineDistance(velocityY)
            if (splingFlingDistance > totalDy) {
                childFling(mFlingHelper.getVelocityDistance(splingFlingDistance - totalDy.toDouble()).toInt())
            }
        }
        totalDy = 0
        velocityY = 0
    }

    //惯性滑动回调这个方法,记录速度，转化成距离，减去自己滑的距离，判断给孩子滑动的距离
    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        if (velocityY <= 0) {
            this.velocityY = 0
        } else {
            isStartFling = true
            this.velocityY = velocityY
        }
    }

    private fun childFling(velocity: Int) {
        mRecyclerView.fling(0, velocity)
    }
}