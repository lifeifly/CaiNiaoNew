package com.example.video.banner

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.video.R


class BannerView : RelativeLayout {

    private lateinit var mBannerVp: BannerViewPager
    private lateinit var mDotContainer: LinearLayout
    private var mAdapter: BannerItemAdaper? = null

    //点选中的drawable
    private var mNormalDrawable: Drawable? = null
    private var mPressDrawable: Drawable? = null

    //当前亮的点、
    private var mCurrentLightPosition: Int? = 0

    //自定义属性
    //圆点大小
    private var mDotSize: Int = 8//dp

    //圆点间距
    private var mDotDistance: Int = 5//dp

    //圆点位置
    private var mDotGravity: Int = -1//left

    //底部颜色,默认透明
    private var mBottomColor: Int = Color.TRANSPARENT

    //宽高比
    private var widthProperties: Float = 0F
    private var heightProperties: Float = 0F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //把布局加载到该布局
        View.inflate(context, R.layout.ui_banner_layout, this)

        initAttributeSet(context, attrs)
        initView()

    }

    /**
     * 初始化自定义属性
     */
    private fun initAttributeSet(context: Context?, attrs: AttributeSet?) {
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.BannerView)
        mDotGravity = typeArray?.getInt(R.styleable.BannerView_dotGravity, mDotGravity)!!
        mDotSize = typeArray.getDimension(
            R.styleable.BannerView_dotIndicatorSize,
            dip2Px(mDotSize.toFloat())
        ).toInt()
        mDotDistance = typeArray.getDimension(
            R.styleable.BannerView_dotIndicatorDistance,
            dip2Px(mDotDistance.toFloat())
        ).toInt()
        mBottomColor = typeArray.getColor(R.styleable.BannerView_bottomColor, mBottomColor)
        mPressDrawable = typeArray.getDrawable(R.styleable.BannerView_dotIndicatorFocusColor)
        if (mPressDrawable == null) {
            mPressDrawable = ColorDrawable(Color.RED)
        }
        mNormalDrawable = typeArray.getDrawable(R.styleable.BannerView_dotIndicatorDefaultColor)
        if (mNormalDrawable == null) {
            //如果没配置
            mNormalDrawable = ColorDrawable(Color.GRAY)
        }

        //获取宽高比
        widthProperties =
            typeArray.getFloat(R.styleable.BannerView_widthProperties, widthProperties)
        heightProperties =
            typeArray.getFloat(R.styleable.BannerView_heightProperties, heightProperties)
        typeArray.recycle()
    }

   private fun initView() {
        mBannerVp = findViewById(R.id.banner_viewpager)

        mDotContainer = findViewById(R.id.dot_container)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (widthProperties == 0F || heightProperties == 0F) {
            return
        }
        //动态指定宽高
        val width = measuredWidth
        val height = (width * (heightProperties / widthProperties)).toInt()
        //指定宽高
        val params = layoutParams
        params.height = height
        Log.d("wh",params.height.toString()+""+params.width)
        layoutParams = params
    }

    fun setAdapter(adaper: BannerItemAdaper) {
        this.mAdapter = adaper
        mBannerVp.setAdapter(adaper)
        initDotIndicator()

        //监听ViewPager的变换切换指示器
        mBannerVp.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //新位置被选中时调用
                indicatorSelected(position)
            }
        })
        Log.d("TAG", height.toString())
    }

    /**
     * 页面切换的回调，选中当前位置的指示器
     */
    private fun indicatorSelected(position: Int) {
        //把之前的设为默认
        val mCurrentDotView =
            mCurrentLightPosition?.let { mDotContainer.getChildAt(it) as DotIndicatorView }
        mCurrentDotView?.setDrawable(mNormalDrawable)
        //把当前的点亮
        val mNextDotView =
            mDotContainer.getChildAt(position % (mAdapter?.getCount()!!)) as DotIndicatorView
        mNextDotView.setDrawable(mPressDrawable)
        mCurrentLightPosition = position % (mAdapter?.getCount()!!)

    }


    /**
     * 开始滚动
     */
    fun startRoll() {
        mBannerVp.startRoll()
    }

    /**
     * 初始化点的指示器
     */

    private fun initDotIndicator() {
        mDotContainer.removeAllViews()
        val count = mAdapter?.getCount()
        if (count != null) {
            for (i in 0..count - 1) {
                //不断的添加圆点
                val dotView = DotIndicatorView(context)
                //设置大小
                val params = LinearLayout.LayoutParams(mDotSize, mDotSize)
                //设置间隔
                params.leftMargin = mDotDistance
                dotView.layoutParams = params
                //设置对齐方式，剧中靠左靠右
                mDotContainer.gravity = getGravityParams()


                if (i == 0) {
                    //默认选中第一个
                    dotView.setDrawable(mPressDrawable)
                } else {
                    dotView.setDrawable(mNormalDrawable)
                }
                mDotContainer.addView(dotView)
            }

        }
    }

    private fun getGravityParams(): Int {
        return when (mDotGravity) {
            -1 -> Gravity.LEFT
            0 -> Gravity.CENTER
            1 -> Gravity.RIGHT
            else -> Gravity.LEFT
        }
    }

    private fun dip2Px(dip: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
    }


}