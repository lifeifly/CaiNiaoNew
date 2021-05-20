package com.example.sjzs.ui.recyclerview

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.marginLeft
import androidx.databinding.BindingAdapter
import com.example.sjzs.R
import com.example.sjzs.databinding.ItemChinaRvBinding
import com.example.sjzs.databinding.ItemCollectRvLayoutBinding
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.ui.flowlayout.SelectView

class ItemCollectView : FrameLayout, View.OnClickListener {
    private val mBind: ItemCollectRvLayoutBinding

    private var mSelectViewMargin = 0
    private val ANIM_DURATION = 250L
    private var isShow = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //加载布局
        mBind = ItemCollectRvLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        mBind.selectView.setOnClickListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mSelectViewMargin = mBind.selectView.marginLeft
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mSelectViewMargin = mBind.selectView.measuredWidth

    }

    fun setClick(itemClick: IItemClick<CollectBean>) {
        mBind.itemClick = itemClick
    }

    fun setCollectBean(collectBean: CollectBean) {
        mBind.collectBean = collectBean
    }

    //显示选择框
    fun showSelect() {
        isShow = !isShow
        if (isShow) {
            //当前状态是show
            //将点击事件功能取消
            mBind.containerCollect.isClickable = false

            val marginAnimator = ObjectAnimator.ofInt(this, "selectMargin", 0, mSelectViewMargin)
            marginAnimator.duration = ANIM_DURATION
            marginAnimator.start()
        }
    }


    //隐藏选择框
    fun hideSelect() {
        isShow = !isShow
        if (!isShow) {
            //当前状态是hide
            //将点击事件开启
            mBind.containerCollect.isClickable = true

            val marginAnimator = ObjectAnimator.ofInt(this, "selectMargin", mSelectViewMargin, 0)
            marginAnimator.duration = ANIM_DURATION
            marginAnimator.start()
        }
    }

    /**
     * 设置select选择
     */
    fun setSelect(isSelect: Boolean) {
        mBind.selectView.setIsSelect(isSelect)
    }

    private fun setSelectMargin(marginLeft: Int) {
        val params = mBind.space.layoutParams as MarginLayoutParams
        params.leftMargin = marginLeft

        mBind.space.layoutParams = params
        invalidate()
    }

    /**
     * 获取当前的状态,是否被选中
     */
    fun getSelectStatu(): Boolean {
        return mBind.selectView.getStatus()
    }

    override fun onClick(v: View?) {
        if (isShow) {
            when (v?.id) {
                R.id.select_view -> {
                    mBind.selectView.startAnim()
                }
            }
        }
    }


    companion object {
        /**
         * 数据绑定
         */
        @BindingAdapter("click")
        @JvmStatic
        fun setItemClick(itemCollectView: ItemCollectView, click: IItemClick<*>) {
            itemCollectView.setClick(click as IItemClick<CollectBean>)
        }

        @BindingAdapter("collectBean")
        @JvmStatic
        fun setCollectBean(itemCollectView: ItemCollectView, collectBean: CollectBean) {
            itemCollectView.setCollectBean(collectBean)
        }

    }
}