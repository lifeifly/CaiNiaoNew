package com.example.sjzs.ui.scrollview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.R
import com.example.video.banner.BannerView

class PagingNestedScrollView : NestedScrollView {
    private lateinit var mBannerView: BannerView
    private lateinit var mRecyclerView: RecyclerView

    var velocityY: Int = 0
    var isStartFling: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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

    //手动实现把bannerview隐藏
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        //向上滑动，若当前topview，可见，需要将topview滑动至不可见
        var isNeedHideTopView = (dy > 0 && scrollY < mBannerView.measuredHeight)
        if (isNeedHideTopView) {
            scrollBy(0, dy)
        }
    }

    //惯性滑动,记录速度，转化成距离，减去自己滑的距离，判断给孩子滑动的距离
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
        mRecyclerView.fling(0, velocityY)

    }
}