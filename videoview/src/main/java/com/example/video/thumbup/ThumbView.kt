package com.example.thumbupsample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.OvershootInterpolator
import com.example.video.R

/**
 * 点赞图片效果
 */
class ThumbView : View {
    companion object {
        //圆圈颜色
        private val START_COLOR = Color.parseColor("#00e24d3d")
        private val END_COLOR = Color.parseColor("#88e24d3d")

        //缩放动画时间
        private val SCALE_DURATION = 150L

        //圆圈扩散动画的时间
        private val RADIUS_DURATION = 100L

        //最小的缩放和最大的缩放比例
        private val MIN_SCALE = 0.9F
        private val MAX_SCALE = 1F
    }

    private var mLastStartTime: Long = 0
    private var mIsThumbUp: Boolean = false

    //点赞bitmap和发光bitmap
    private lateinit var mThumbUp: Bitmap
    private lateinit var mShining: Bitmap
    private lateinit var mThumbUpNormal: Bitmap
    private lateinit var mBitmapPaint: Paint

    //图片的宽度
    private var mThumbWidth = 0
    private var mThumbHeight = 0
    private var mShiningWidth = 0
    private var mShiningHeight = 0

    private lateinit var mThumbPoint: CvPoint
    private lateinit var mCirclePoint: CvPoint
    private lateinit var mShiningPoint: CvPoint

    private var mRadiusMax = 0F
    private var mRadiusMin = 0F
    private var mRadius = 0F
    private var mClipPath: Path? = null
    private lateinit var mCirclePaint: Paint

    //点击的回调
    private var mThumbUpClickListener: ThumbUpClickListener? = null

    //被点击的次数和，未点击时，未点赞是0，点赞是1所以点完之后的次数是偶数则就是未点赞，奇数就是点赞
    private var mClickCount = 0
    private var mEndCount = 0
    private var mThumbUpAnim: AnimatorSet? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initData()
    }

    fun initData() {
        //初始化bitmap和画笔
        mBitmapPaint = Paint()
        mBitmapPaint.isAntiAlias = true
        resetBitmap()

        mThumbWidth = mThumbUp.width
        mThumbHeight = mThumbUp.height
        mShiningWidth = mShining.width
        mShiningHeight = mShining.height

        mShiningPoint = CvPoint()
        mCirclePoint = CvPoint()
        mThumbPoint = CvPoint()

        //相对位置
        mShiningPoint.setX(paddingLeft + dp2Px(context, 2F))
        mShiningPoint.setY(paddingTop.toFloat())

        mThumbPoint.setX(paddingLeft.toFloat())
        mThumbPoint.setY(paddingTop + dp2Px(context, 8F))

        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = dp2Px(context, 2F)

        mCirclePoint = CvPoint()
        mCirclePoint.setX(mThumbPoint.getX() + mThumbWidth / 2)
        mCirclePoint.setY(mThumbPoint.getY() + mThumbHeight / 2)

        mRadiusMax = Math.max(mCirclePoint.getX() - paddingLeft, mCirclePoint.getY() - paddingTop)
        mRadiusMin = dp2Px(context, 8F)

        mClipPath = Path()
        mClipPath?.addCircle(
            mCirclePoint.getX(),
            mCirclePoint.getY(),
            mRadiusMax,
            Path.Direction.CW
        )
    }

    private fun resetBitmap() {
        mThumbUp = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mThumbUpNormal =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mShining =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
    }

    fun setIsThumbUp(isThumbUp: Boolean) {
        this.mIsThumbUp = isThumbUp
        mClickCount = if (mIsThumbUp) 1 else 0
        mEndCount = mClickCount
        postInvalidate()
    }

    fun getIsThumbUp(): Boolean {
        return mIsThumbUp
    }

    fun setThumbUpListener(thumbUpClickListener: ThumbUpClickListener) {
        this.mThumbUpClickListener = thumbUpClickListener
    }

    fun getCirclePoint(): CvPoint {
        return mCirclePoint
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getOtherDefaultSize(getContentWidth() + paddingLeft + paddingRight, widthMeasureSpec),
            getOtherDefaultSize(getContentHeight() + paddingLeft + paddingRight, heightMeasureSpec)
        )
    }

    private fun getContentHeight(): Int {
        val minTop = Math.min(mShiningPoint.getY(), mThumbPoint.getY())
        val maxBottom =
            Math.max(mShiningPoint.getY() + mShiningHeight, mThumbPoint.getY() + mThumbHeight)
        return (maxBottom - minTop).toInt()

    }

    private fun getContentWidth(): Int {
        val minLeft = Math.min(mShiningPoint.getX(), mThumbPoint.getX())
        val maxRight =
            Math.max(mShiningPoint.getX() + mShiningWidth, mThumbPoint.getX() + mThumbWidth)
        return (maxRight - minLeft).toInt()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superData", super.onSaveInstanceState())
        bundle.putBoolean("isThumbUp", mIsThumbUp)

        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val data: Bundle = state as Bundle
        val superData: Parcelable? = data.getParcelable("superData")

        super.onRestoreInstanceState(superData)

        mIsThumbUp = data.getBoolean("isThumbUp")

        initData()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsThumbUp) {
            if (mClipPath != null) {
                canvas?.save()
                canvas?.clipPath(mClipPath!!)
                canvas?.drawBitmap(
                    mShining,
                    mShiningPoint.getX(),
                    mShiningPoint.getY(),
                    mBitmapPaint
                )
                canvas?.restore()
                canvas?.drawCircle(mCirclePoint.getX(), mCirclePoint.getY(), mRadius, mCirclePaint)
            }
            canvas?.drawBitmap(mThumbUp, mThumbPoint.getX(), mThumbPoint.getY(), mBitmapPaint)
        } else {
            canvas?.drawBitmap(mThumbUpNormal, mThumbPoint.getX(), mThumbPoint.getY(), mBitmapPaint)
        }
    }

    fun startAnim() {
        mClickCount++
        var isFastAnim = false
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastStartTime < 300) {
            isFastAnim = true
        }
        mLastStartTime = currentTime
        if (mIsThumbUp) {//当前状态是点赞状态
            if (isFastAnim) {
                startFastAnim()
                return
            }

            startThumbDownAnim()
            mClickCount = 0//置为0表示处于未点赞状态
        } else {
            if (mThumbUpAnim != null) {
                mClickCount = 0//代表正在执行点赞动画，不用重复
            } else {
                startThumbUpAnim()//没有执行动画
                mClickCount = 1//置为1表示处于点赞状态
            }
        }
        mEndCount = mClickCount
    }

    private fun startFastAnim() {
        val thumbUpScale = ObjectAnimator.ofFloat(this, "thumbUpScale", MIN_SCALE, MAX_SCALE)
        thumbUpScale.duration = SCALE_DURATION
        thumbUpScale.interpolator = OvershootInterpolator()

        val circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax)
        circleScale.duration = RADIUS_DURATION
        //连续多次点击
        val animationSet = AnimatorSet()
        animationSet.playTogether(thumbUpScale, circleScale)
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mEndCount++//每次点击+1
                //只有最后一次点击后mClickCount才会和mEndCount相等
                if (mClickCount != mEndCount) {
                    return
                }
                if (mClickCount % 2 == 0) {
                    startThumbDownAnim()
                } else {
                    if (mThumbUpClickListener != null) {
                        mThumbUpClickListener?.thumbUpFinish()
                    }
                }
            }
        })

        animationSet.start()

    }

    private fun startThumbDownAnim() {
        val thumbUpScale = ObjectAnimator.ofFloat(this, "thumbUpScale", MIN_SCALE, MAX_SCALE)
        thumbUpScale.duration = SCALE_DURATION
        //先让点赞图标缩放，缩放完成后设置其画未点赞图标
        thumbUpScale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mIsThumbUp = false
                setNotThumbScale(MAX_SCALE)
                if (mThumbUpClickListener != null) {
                    mThumbUpClickListener?.thumbDownFinish()
                }
            }
        })
        thumbUpScale.start()
    }

    fun startThumbUpAnim() {
        val notThumbUpScale = ObjectAnimator.ofFloat(this, "notThumbScale", MAX_SCALE, MIN_SCALE)
        notThumbUpScale.duration = SCALE_DURATION
        notThumbUpScale.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mIsThumbUp = true

            }


        })

        val thumbUpScale = ObjectAnimator.ofFloat(this, "thumbUpScale", MIN_SCALE, MAX_SCALE)
        thumbUpScale.duration = SCALE_DURATION
        thumbUpScale.interpolator = OvershootInterpolator()


        val circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax)
        circleScale.duration = RADIUS_DURATION


        mThumbUpAnim = AnimatorSet()
        mThumbUpAnim?.play(thumbUpScale)?.with(circleScale)
        mThumbUpAnim?.play(thumbUpScale)?.after(notThumbUpScale)//在该动画结束之后开始参考动画thumbScale
        mThumbUpAnim?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mThumbUpAnim = null
                if (mThumbUpClickListener != null) {
                    mThumbUpClickListener?.thumbUpFinish()
                }
            }
        })
        mThumbUpAnim?.start()
    }

    private fun setNotThumbScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mThumbUpNormal =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mThumbUpNormal = Bitmap.createBitmap(
            mThumbUpNormal,
            0,
            0,
            mThumbUpNormal.width,
            mThumbUpNormal.height,
            matrix,
            true
        )
        postInvalidate()
    }

    private fun setThumbUpScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mThumbUp = BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mThumbUp =
            Bitmap.createBitmap(mThumbUp, 0, 0, mThumbUp.width, mThumbUp.height, matrix, true)
        postInvalidate()
    }

    fun setCircleScale(radius: Float) {
        mRadius = radius
        mClipPath = Path()
        mClipPath?.addCircle(mCirclePoint.getX(), mCirclePoint.getY(), mRadius, Path.Direction.CW)
        val fraction = (mRadiusMax - mRadius) / (mRadiusMax - mRadiusMin)
        mCirclePaint.color = evaluate(fraction, START_COLOR, END_COLOR)
        postInvalidate()
    }

    fun setShiningScale(scale: Float) {
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        mShining =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
        mShining =
            Bitmap.createBitmap(mShining, 0, 0, mShining.width, mShining.height, matrix, true)
        postInvalidate()
    }

    interface ThumbUpClickListener {
        //点赞回调
        fun thumbUpFinish()

        //取消回调
        fun thumbDownFinish()
    }
}