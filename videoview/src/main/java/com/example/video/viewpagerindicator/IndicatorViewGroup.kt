package com.example.video.viewpagerindicator

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout

class IndicatorViewGroup : FrameLayout {
    //存放上部指示器的linearLayout
    private val mIndicatorContainer: LinearLayout

    //下部指示器
    private  var mIndicator: View?=null

    //每个text的宽度
    private var mItemWidth = 0

    private lateinit var mParams: FrameLayout.LayoutParams

    //底部指示器的宽度
    private var mBottomIndicatorWidth = 0


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mIndicatorContainer = LinearLayout(context)
        //添加到该容器
        addView(mIndicatorContainer)
    }

    /**
     * 添加文字指示view到mIndicatorContainer
     */
    fun addIndicatorItem(itemView: View) {
        mIndicatorContainer.addView(itemView)
    }

    /**
     * 获取对应位置的itemview
     */
    fun getItemView(position: Int): View {
        return mIndicatorContainer.getChildAt(position)
    }

    /**
     * 添加底部指示器
     */
    fun addBottomIndicator(view: View, itemWidth: Int) {
        this.mIndicator = view
        this.mItemWidth = itemWidth
        mBottomIndicatorWidth = itemWidth / 3

        //添加到该容器
        mParams = view.layoutParams as LayoutParams
        mParams.gravity = Gravity.BOTTOM
        mIndicator?.layoutParams = mParams
        if (view is ElongationCompressionView){
            view.setWidthHeight(mBottomIndicatorWidth,10,mItemWidth)
        }
        addView(mIndicator)
    }

    /**
     * 滚动底部指示器
     */
    fun scrollBottomIndicator( position: Int, positionOffset: Float) {

        //向左滑
        if (mIndicator!=null&&mIndicator is ElongationCompressionView){
            (mIndicator as ElongationCompressionView).setStretchMargin(position, positionOffset)
        }

    }

    /**
     * 让文本颜色随滑动改变
     */
    fun scrollHeaderTextIndicator(position: Int, positionOffset: Float) {
        if (position!=mIndicatorContainer.childCount-1){//防止滑到最后一个超出数量
            val left=getItemView(position) as ColorTrackTextView
            //设置方向
            left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEGT)
            //设置进度
            left.setCurrentProgress(1-positionOffset)

            val right=getItemView(position+1) as ColorTrackTextView
            right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT)
            right.setCurrentProgress(positionOffset)
        }
    }

}