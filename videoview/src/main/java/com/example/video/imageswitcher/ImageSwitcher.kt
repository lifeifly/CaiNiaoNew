package com.example.video.imageswitcher

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.ImageSwitcher
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.banner.BannerPagerAdapter
import com.example.video.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlin.math.log

/**
 * 图片查看器
 */
class ImageSwitcher : FrameLayout, View.OnClickListener {
    private val mIbLeft: ImageButton
    private val mIbRight: ImageButton
    private val mIvSwitcher: ImageSwitcher
    private val mDigitalView: DigitalView
    private val mTvContent: TextView
    private val mGallery: ImageViewPagerGallery
    private lateinit var mAdapter: BannerPagerAdapter

    //总数
    private var mTotal = 0

    //当前位置,从1开始
    private var mCur = 0

    //图片地址的集合
    private lateinit var mPhotos: MutableList<PhotoListBean>

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //加载布局
        View.inflate(context, R.layout.image_switch_layout, this)
        mIbLeft = findViewById(R.id.arrow_left)
        mIbLeft.setOnClickListener(this)
        mIbRight = findViewById(R.id.arrow_right)
        mIbRight.setOnClickListener(this)
        mIvSwitcher = findViewById(R.id.iv_switcher)
        mDigitalView = findViewById(R.id.digital_view)
        mTvContent = findViewById(R.id.tv_content)
        mGallery = findViewById(R.id.gallery)
        initImageButton()

    }


    private fun initImageButton() {
        val leftAnimation = ObjectAnimator.ofFloat(mIbLeft, "alpha", 0F, 1F)
        leftAnimation.repeatCount = -1
        leftAnimation.repeatMode = ValueAnimator.REVERSE
        val rightAnimation = ObjectAnimator.ofFloat(mIbRight, "alpha", 0F, 1F)
        rightAnimation.repeatCount = -1
        rightAnimation.repeatMode = ValueAnimator.REVERSE
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(leftAnimation, rightAnimation)
        animatorSet.duration = 1500
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
    }



    /**
     * 为ViewPager设置适配器
     */
    private fun setAdapter(adapter: BannerPagerAdapter) {
        mAdapter = adapter
        initImageSwitcher()
        initDigitalView()
        mTotal = mAdapter.getCount()
        mGallery.setAdapter(adapter)
        //监听ViewPager设置图片
        mGallery.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                //在0处设置left不可见，最后处设置right不可见
                if (position == 0) {
                    mIbLeft.visibility = View.INVISIBLE
                } else if (position == mAdapter.getCount() - 1) {
                    mIbRight.visibility = View.INVISIBLE
                } else {
                    mIbLeft.visibility = View.VISIBLE
                    mIbRight.visibility = View.VISIBLE
                }
                //设置动画
                if (mCur - 1 > position) {
                    //右移
                    mIvSwitcher.outAnimation =
                        AnimationUtils.loadAnimation(context, R.anim.right_out)
                    mIvSwitcher.inAnimation = AnimationUtils.loadAnimation(context, R.anim.left_in)
                } else if (mCur - 1 < position) {
                    //左移
                    mIvSwitcher.outAnimation =
                        AnimationUtils.loadAnimation(context, R.anim.left_out)
                    mIvSwitcher.inAnimation = AnimationUtils.loadAnimation(context, R.anim.right_in)
                }
                //设置对应的图片
                setImageSwitcherDrawable(mPhotos.get(position).photoUrl)
                mTvContent.setText(mPhotos[position].des)
                mDigitalView.setCurrent(position + 1)
                mCur = position + 1
            }
        })
    }

    private fun setImageSwitcherDrawable(url: String) {
        Observable.just(url)
            .map {
                val bitmap=Glide.with(context).asDrawable().load(url).submit().get()
                bitmap
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object :Consumer<Drawable>{
                override fun accept(t: Drawable?) {
                    mIvSwitcher.setImageDrawable(t)
                }
            })
    }

    private fun initDigitalView() {
        mDigitalView.setTotal(mAdapter.getCount())
    }
    lateinit var imageView:ImageView
    /**
     * 初始化ImageIwitcher
     */
    private fun initImageSwitcher() {
        mIvSwitcher.setFactory {
            val imageView = ImageView(context)
            imageView.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType=ImageView.ScaleType.FIT_XY
            imageView
        }
        mIbLeft.visibility=View.INVISIBLE
        setImageSwitcherDrawable(mPhotos[mCur].photoUrl)
        mTvContent.setText(mPhotos[mCur].des)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.arrow_left -> {
                if (mGallery.currentItem > 0) {
                    mGallery.setCurrentItem(mGallery.currentItem - 1)
                }
            }
            R.id.arrow_right -> {
                if (mGallery.currentItem < mAdapter.getCount() - 1) {
                    mGallery.setCurrentItem(mGallery.currentItem + 1)
                }
            }
        }
    }


    fun setPhotoList(photoLists: MutableList<PhotoListBean>) {
        this.mPhotos = photoLists
        setAdapter(object : BannerPagerAdapter() {
            override fun getView(position: Int, convertView: View?): View {
                val imageView = ImageView(context)
                val url = mPhotos.get(position).photoUrl
                Glide.with(context).load(url).into(imageView)
                return imageView
            }

            override fun getCount(): Int {
                return mPhotos.size
            }
        })
    }

}