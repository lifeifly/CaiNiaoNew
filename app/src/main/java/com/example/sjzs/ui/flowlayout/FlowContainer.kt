package com.example.sjzs.ui.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.sjzs.R
import com.example.sjzs.model.bean.SearchHistory

class FlowContainer : FrameLayout, View.OnClickListener {
    private lateinit var mFlow: FlowLayout
    private lateinit var mIvPull: ImageView
    private lateinit var mIvDelete: ImageView
    private var isShow = true
    private lateinit var mTvComplete:TextView
    private lateinit var mTvDeleteAll:TextView
    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //加载布局
        View.inflate(context, R.layout.flow_history_layout, this)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        mFlow = findViewById(R.id.flowLayout2)
        mIvPull = findViewById(R.id.show_hide_history)
        mIvPull.setOnClickListener(this)
        mIvDelete = findViewById(R.id.iv_delete)
        mIvDelete.setOnClickListener(this)
        mTvComplete=findViewById(R.id.tv_complete)
        mTvComplete.setOnClickListener(this)
        mTvDeleteAll=findViewById(R.id.tv_deleteAll)
        mTvDeleteAll.setOnClickListener(this)

        mFlow.setParent(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.show_hide_history -> {
                mIvPull.animate().rotation(180F).setDuration(1000)
                if (isShow) {
                    isShow=false
                    mFlow.visibility = View.GONE
                } else {
                    isShow=true
                    mFlow.visibility = View.VISIBLE
                }
            }
            R.id.iv_delete -> {
                showDelete()
            }
            R.id.tv_deleteAll->{
                deleteAll()
                reset()
            }
            R.id.tv_complete->{
                reset()
            }
        }
    }

    /**
     * 显示删除按钮
     */
    fun showDelete(){
        //隐藏当前图标
        mIvDelete.visibility=View.INVISIBLE
        //显示全部删除和完成按钮
        mTvComplete.visibility=View.VISIBLE
        mTvDeleteAll.visibility=View.VISIBLE
        //显示每个子view的删除图标
        mFlow.showDelete()
    }

    fun reset(){
        //隐藏子view的删除图标
        mFlow.hideDelete()
        //隐藏全部删除和完成按钮
        mTvDeleteAll.visibility=View.GONE
        mTvComplete.visibility=View.GONE
        //显示删除的图标
        mIvDelete.visibility=View.VISIBLE
    }
    private fun deleteAll(){
        mFlow.deleteAll()
    }

    fun addText(text: String) {
        mFlow.addSingleText(text)
    }

    fun addTexts(histories: List<SearchHistory>?) {
        mFlow.addTexts(histories)
    }


}