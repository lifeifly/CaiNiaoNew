package com.example.sjzs.ui.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.room.withTransaction
import com.example.sjzs.model.bean.SearchHistory
import com.example.sjzs.model.datastore.key
import com.example.sjzs.model.room.ListBeanDatabase
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.max

/**
 * 流式布局
 */
class FlowLayout : ViewGroup {
    private val texts: MutableList<SearchTextView> = mutableListOf()
    private val indexs: MutableList<String> = mutableListOf()
    private val DISTANCE = 20
    private var isShowDelete = false

    private var parent: FlowContainer? = null
    private val database:ListBeanDatabase
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        database= ListBeanDatabase.getDatabase(context?.applicationContext!!)
    }

    fun setParent(parent: FlowContainer){
        this.parent=parent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (texts.size == 0) return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = 0F
        val width = parentWidth
        var tempWidth = 0
        var tempHeight = 0F
        if (parentHeightMode == MeasureSpec.EXACTLY) {
            height = parentHeight.toFloat()
        } else {
            for (i in 0..childCount - 1) {
                val textView = getChildAt(i) as SearchTextView
                measureChild(textView, widthMeasureSpec, heightMeasureSpec)
                if (i == 0) {
                    //第一个view先将高度加起来
                    tempHeight = textView.measuredHeight.toFloat()
                    height += textView.measuredHeight.toFloat()
                }
                if (paddingLeft + paddingRight + tempWidth + textView.measuredWidth + DISTANCE > width) {
                    //需要换行高度累加
                    height += textView.measuredHeight + DISTANCE
                    //记录下一行第一个view的高度
                    tempHeight = textView.measuredHeight.toFloat()
                    //记录下一行第一个view的宽度
                    tempWidth = textView.measuredWidth
                } else {
                    if (tempHeight < textView.measuredHeight) {
                        //如果不用换行，上次记录的高度比这个小，就减去之前的高度再加上现在的高度
                        height = height - tempHeight + textView.measuredHeight
                        //将较高的高度赋值给临时高度
                        tempHeight = textView.measuredHeight.toFloat()
                    }
                    //不换行就相加当前高度
                    tempWidth += textView.measuredWidth + DISTANCE
                }
            }
            height += (paddingTop + paddingBottom)
        }
        setMeasuredDimension(width, height.toInt())
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //布局
        if (childCount > 0) {
            var hegith = paddingTop
            var width = paddingLeft
            var maxHegiht = 0
            for (i in 0..childCount - 1) {
                val textView = getChildAt(i) as SearchTextView
                val textWidth = textView.measuredWidth
                val textHeight = textView.measuredHeight
                if (width + textWidth + paddingRight < measuredWidth) {
                    Log.d("TAG", "onLayout: " + l + "/" + t)
                    //不用换行,直接布局
                    textView.layout(
                        width,
                        hegith,
                        width + textWidth,
                        hegith + textHeight
                    )
                    //获取一行中最高的高度
                    maxHegiht = Math.max(maxHegiht.toFloat(), textHeight.toFloat()).toInt()
                    //宽度右移
                    width += textWidth + DISTANCE
                } else {
                    //换行
                    //将上一行中的最高高度累加
                    hegith += (maxHegiht + DISTANCE)
                    //记录当前行第一个view的高度
                    maxHegiht = textHeight
                    //宽度重置为paddingleft
                    width = paddingLeft
                    textView.layout(
                        width,
                        hegith,
                        width + textWidth,
                        hegith + textHeight
                    )
                    //布局完后将当前width相加
                    width += textWidth + DISTANCE
                }
            }
        }
    }

    /**
     * 添加字符
     */
    fun addSingleText(text: String) {
        textAdded(text)
        invalidate()
    }

    private fun textAdded(text: String) {
        val textView = SearchTextView(context)
        textView.setTag(text)
        textView.setOnClickListener { v ->
            if (isShowDelete) {//显示了删除按钮
                //删除该试图
                deleteText(v.tag as String)
            } else {
                //开始搜索

            }
        }

        textView.setOnLongClickListener{v ->
            //如果之前没有显示删除，则显示删除
            if (!isShowDelete){
                parent?.showDelete()
                true
            }
            else{
                false
            }
        }
        textView.setText(text)
        addView(textView)
        texts.add(textView)
        indexs.add(text)
    }

    /**
     * 删除字符
     */
    fun deleteText(text: String) {
        val textview = texts.get(indexs.indexOf(text))
        texts.remove(textview)
        removeView(textview)
        indexs.remove(text)
        invalidate()
        //从数据库中删除对应数据
        GlobalScope.launch(Dispatchers.IO) {
            val dao= database.historyDao()
            database.withTransaction {
                dao.deleteListBeans(text)
            }
        }

        if (childCount == 0) {
            //如果删除之后没有子view，则关闭编辑界面，显示删除按钮
            parent?.reset()
        }
    }

    /**
     * 删除所有视图
     */
    fun deleteAll() {
        texts.clear()
        indexs.clear()
        removeAllViews()
        invalidate()
        //从数据库中删除所有数据
        GlobalScope.launch(Dispatchers.IO) {
            val dao= database.historyDao()
            database.withTransaction {
                dao.deleteAllListBeans()
            }
        }
    }

    /**
     * 显示所有子view的删除图标
     */
    fun showDelete() {
        isShowDelete = true
        texts.forEach {
            it.setShowDelete(true)
        }
    }

    /**
     * 隐藏所有的子View的删除图标
     */
    fun hideDelete() {
        isShowDelete = false
        texts.forEach {
            it.setShowDelete(false)
        }
    }

    fun addTexts(histories: List<SearchHistory>?) {
        histories?.forEach {
            textAdded(it.keyword)
        }
        invalidate()
    }


}