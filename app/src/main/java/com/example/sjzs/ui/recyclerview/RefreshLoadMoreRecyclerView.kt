package com.example.sjzs.ui.recyclerview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.marginTop
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.ui.adapter.ChinaPagingAdapter
import com.example.sjzs.ui.adapter.base.BaseBindingAdapter

class RefreshLoadMoreRecyclerView : RecyclerView {
    //当前是否在拖动
    private var isDrag: Boolean = false

    //header高度
    private var refreshViewHeight = 0
    private var mHeaderView: View? = null

    //手指按下的位置
    private var mFingerDownY = 0

    //上次刷新的加载状态
    private var mPreRefreshState: LoadState? = null

    //上次加载更多的状态
    private var mPreLoadMoreState: LoadState? = null

    companion object {
        //默认状态
        private val REFRESH_NORMAL = 0X0011

        //下拉刷新状态
        private val REFRESH_PULL_DOWN = 0X0022

        //松开刷新状态
        private val REFRESH_LOOSEN_REFRESHING = 0X0033

        //正在刷新状态
        private val REFRESH_LOADING = 0X0044


        //当前的状态
        private var mCurrentRefreshStatus = REFRESH_NORMAL

        //拖拽阻力系数
        private val DRAG_RESISTANCE = 0.35F
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is BaseBindingAdapter<*, *>) {
            //监听refresh
            adapter.addLoadStateListener {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        if (mPreRefreshState == null) {
                            //初次加载,显示上拉刷新view
                            setRefreshViewMarginTop(0)
                        }
                        if (mPreRefreshState != LoadState.Loading) {
                            mPreRefreshState = LoadState.Loading
                            adapter.startRefresh()
                        }
                    }
                    is LoadState.NotLoading -> {
                        if (mPreRefreshState != null && mPreRefreshState is LoadState.Loading) {//上次是loading这次是NOTLOADING说明是完整的一个流程
                            adapter.showRefreshSuccess()
                            //两秒后关闭
                            postDelayed({
                                onStopRefresh()
                            }, 1500)

                        }
                        if (mPreRefreshState != null) {
                            //说明不是首次加载,可以为其赋值
                            mPreRefreshState = LoadState.NotLoading(false)
                        }

                    }
                    is LoadState.Error -> {
                        if (mPreRefreshState != null && mPreRefreshState is LoadState.Loading) {//上次是loading这次是NOTLOADING说明是完整的一个流程
                            adapter.showRefreshError()

                        }
                        if (mPreRefreshState != null) {
                            //说明不是首次加载,可以为其赋值
                            mPreRefreshState =
                                LoadState.Error((it.refresh as LoadState.Error).error)
                        }

                    }
                }


                //监听加载更多
                when (it.append) {
                    is LoadState.Loading -> {
                        if (mPreLoadMoreState == null) {
                            //初次加载,显示加载更多View

                        }
                        if (!(mPreRefreshState is LoadState.Error)&&mPreLoadMoreState!=LoadState.Loading ) {
                            mPreLoadMoreState = LoadState.Loading
                            if (childCount>0){
                                adapter.startLoadMore()
                            }
                        }
                    }
                    is LoadState.NotLoading -> {
                        if (mPreLoadMoreState != null && mPreLoadMoreState is LoadState.Loading) {//上次是loading这次是NOTLOADING说明是完整的一个流程

                            if (it.append.endOfPaginationReached) {
                                //说明没数据了
                                adapter.showLoadMoreNoData()
                                return@addLoadStateListener
                            }
                            adapter.resetLoadMore()
                        }
                        if (mPreLoadMoreState != null) {
                            //说明不是首次加载,可以为其赋值
                            mPreLoadMoreState = LoadState.NotLoading(false)
                        }

                    }
                    is LoadState.Error -> {
                        if (!(mPreRefreshState is LoadState.Error) && mPreLoadMoreState != null && mPreLoadMoreState is LoadState.Loading) {
                            //上次是loading这次是NOTLOADING说明是完整的一个流程,如果上次error被refresh拦截，就不用处理
                            adapter.showLoadMoreError()
                        }
                        if (mPreLoadMoreState != null) {
                            //说明不是首次加载,可以为其赋值
                            mPreLoadMoreState =
                                LoadState.Error((it.append as LoadState.Error).error)
                        }
                    }
                }
            }
            super.setAdapter(adapter)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {

                mFingerDownY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_UP -> {

                if (isDrag) {
                    resetHeaderView()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        when (e?.action) {
            MotionEvent.ACTION_MOVE -> {
                //如果可以向上滑或者正在刷新
                if (canScrollVertical(-1) || mCurrentRefreshStatus == REFRESH_LOADING) {
                    //什么都不做
                    return super.onTouchEvent(e)
                }

                //如果当前正在拖动滑动到0位置
                if (isDrag) {
                    scrollToPosition(0)
                }

                //获取手指拖拽的距离乘以阻力系数
                val dy = (e.rawY - mFingerDownY) * DRAG_RESISTANCE
                //如果到达了顶部，并且不断下拉，不断改变margin
                if (dy > 0) {
                    val marginTop = dy - refreshViewHeight
                    setRefreshViewMarginTop(marginTop.toInt())
                    updateRefreshStatus(marginTop.toInt())
                    isDrag = true
                }
            }
        }
        return super.onTouchEvent(e)
    }

    /**
     * 停止刷新
     */
    private fun onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_NORMAL
        resetHeaderView()

    }

    /**
     * 更新状态
     */
    private fun updateRefreshStatus(marginTop: Int) {
        if (marginTop <= -refreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_NORMAL
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_PULL_DOWN//下拉状态
        } else {
            mCurrentRefreshStatus = REFRESH_LOOSEN_REFRESHING
        }
    }

    /**
     * 重置当前刷新状态
     */
    private fun resetHeaderView() {
        val currentTopMargin = mHeaderView?.marginTop
        var finalTopMargin = -refreshViewHeight + 1
        if (mCurrentRefreshStatus == REFRESH_LOOSEN_REFRESHING) {
            //手指松开
            finalTopMargin = 0
            mCurrentRefreshStatus = REFRESH_LOADING//置为正在刷新状态
            //开始执行刷新操作
            if (adapter != null && adapter is PagingDataAdapter<*, *>) {
                (adapter as PagingDataAdapter<*, *>).refresh()
            }
        }
        //回弹到指定位置
        val animator =
            ObjectAnimator.ofFloat(currentTopMargin!!.toFloat(), finalTopMargin.toFloat())
        animator.duration = 1000
        animator.addUpdateListener {
            val currentTopMargin = animator.animatedValue as Float
            setRefreshViewMarginTop(currentTopMargin.toInt())
        }
        animator.start()
        isDrag = false
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            mHeaderView = getChildAt(0)
            Log.d("TAG456", "onMeasure: "+measuredWidth+"/"+getChildAt(0).measuredWidth)
            refreshViewHeight = mHeaderView?.measuredHeight ?: return
            if (refreshViewHeight > 0) {
                //隐藏头部刷新的View marginTop 多留出1px防止无法判断是不是滚动头部问题
                setRefreshViewMarginTop(-refreshViewHeight + 1)
            }
        }
    }

    private fun setRefreshViewMarginTop(marginTop: Int) {
        mHeaderView?.layoutParams?:return
        val params = mHeaderView?.layoutParams as MarginLayoutParams
        var marginTopNeeded = 0
        if (marginTop < -refreshViewHeight + 1) {
            marginTopNeeded = -refreshViewHeight + 1
        } else {
            marginTopNeeded = marginTop
        }
        if (marginTopNeeded == -refreshViewHeight + 1) {
            if (adapter != null && adapter is ChinaPagingAdapter) {
                (adapter as ChinaPagingAdapter).resetRefresh()
            }

        }
        params.topMargin = marginTopNeeded
        mHeaderView?.layoutParams = params
    }

    /**
     * 判断垂直方向是否继续滑动,-1代表反方向，1代表正方向
     */
    fun canScrollVertical(direction: Int): Boolean {
        return canScrollVertically(direction)
    }


}