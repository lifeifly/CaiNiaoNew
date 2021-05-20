package com.example.banner

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.video.R

class BannerView : FrameLayout {
    //圆点指示器的容器
    private val mIndicatorContainer: LinearLayout
    private val mViewPager: MyViewPager
    private lateinit var mAdapter: BannerPagerAdapter

    //点的间距
    private var mDotDis = 0

    //点的默认颜色
    private var mDefaultColor = Color.WHITE

    //点的选中颜色
    private var mSelectedColor = Color.BLUE

    //点的半径
    private var mDotRadius = 20

    //指示器的位置0在中间1在左2在右
    private var mGravity = 0

    //指示器的背景颜色
    private var mIndicatorBg = Color.TRANSPARENT

    //记录上次指示器的位置
    private var mCurrentPosition = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        initAttrs(attrs)
        //创建ViewPager加入其中
        mViewPager = MyViewPager(context)
        val vpParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(mViewPager, vpParams)
        mIndicatorContainer = LinearLayout(context)
        //圆点指示器的LayoutParams,宽度和父容器相同，高度包裹
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            4 * mDotRadius
        )
        //和下部对齐
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        //添加到该view中
        addView(mIndicatorContainer, params)
    }

    /**
     * 初始化自定义属性
     */
    private fun initAttrs(attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        //获取点的半径
        mDotRadius =
            typeArray.getDimensionPixelOffset(R.styleable.BannerView_indicatorRadius, mDotRadius)
        //获取点的间距
        mDotDis = typeArray.getDimensionPixelOffset(R.styleable.BannerView_dotDistance, mDotDis)
        //默认颜色
        mDefaultColor = typeArray.getColor(R.styleable.BannerView_defaultColor, mDefaultColor)
        //选中颜色
        mSelectedColor = typeArray.getColor(R.styleable.BannerView_selectedColor, mSelectedColor)
        //背景颜色
        mIndicatorBg = typeArray.getColor(R.styleable.BannerView_bottomColor, mIndicatorBg)
        //点的位置
        mGravity = typeArray.getInt(R.styleable.BannerView_dotGravity, mGravity)

        typeArray.recycle()
    }


    /**
     * 为viewpager设置适配器
     */
    fun setAdapter(adapter: BannerPagerAdapter) {
        this.mAdapter = adapter
        //初始化指示器
        initIndicator()
        mViewPager.setAdapter(adapter)

    }

    /**
     * 初始化指示器
     */
    private fun initIndicator() {
        if (mIndicatorContainer.childCount!=mAdapter.getCount()){
            for (i in 0..mAdapter.getCount() - 1) {
                val params = LinearLayout.LayoutParams(mDotRadius * 2, mDotRadius * 2)
                params.rightMargin = mDotDis
                val view = CircleView(context)
                //默认选中第一个
                if (i==0){
                    view.setColor(mSelectedColor)
                }else{
                    view.setColor(mDefaultColor)
                }
                mIndicatorContainer.addView(view, params)
            }
            //设置对齐方式
            mIndicatorContainer.gravity = getGravity(mGravity)
            monitorViewPagerScroll()
        }
    }

    /**
     * 监听viewpager的滚动
     */
    private fun monitorViewPagerScroll() {
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val sPosition = position % mAdapter.getCount()
                val circleView = mIndicatorContainer.getChildAt(sPosition)
                val lastIndicator = mIndicatorContainer.getChildAt(mCurrentPosition)
                if (circleView != null && circleView is CircleView) {
                    //选中
                    if (lastIndicator != null && lastIndicator is CircleView) {
                        lastIndicator.setColor(mDefaultColor)
                    }
                    circleView.setColor(mSelectedColor)
                    mCurrentPosition = sPosition

                }
            }
        })
    }

    private fun getGravity(gravityIndex: Int): Int {
        return when (gravityIndex) {
            0 -> Gravity.CENTER
            1 -> Gravity.LEFT
            else -> Gravity.RIGHT
        }
    }
}