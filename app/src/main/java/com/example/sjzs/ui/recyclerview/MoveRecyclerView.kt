package com.example.sjzs.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.R
import com.example.sjzs.ui.flowlayout.SelectView
import kotlinx.android.synthetic.main.item_collect_rv_layout.view.*

class MoveRecyclerView : RecyclerView {

    private var itemTouchStatus: ItemTouchStatus? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setItemTouchStatus(itemTouchStatus: ItemTouchStatus) {
        this.itemTouchStatus = itemTouchStatus
    }

    /**
     * 显示选中提示框
     */
    fun showSelect() {
        for (i in 0..childCount - 1) {
            val itemCollectView = getChildAt(i)
            if (itemCollectView != null && itemCollectView is ItemCollectView) {
                itemCollectView.showSelect()
            }
        }
    }

    /**
     * 取消选中提示框
     */
    fun hideSelect() {
        for (i in 0..childCount - 1) {
            val itemCollectView = getChildAt(i)
            if (itemCollectView != null && itemCollectView is ItemCollectView) {
                itemCollectView.hideSelect()
            }
        }
    }

    /**
     * 删除
     */
    private fun deleteItem(viewHolder: ViewHolder) {
        if (itemTouchStatus != null) {
            itemTouchStatus?.onItemRemove(viewHolder)
        }
    }

    /**
     * 全选
     */
    fun selectAll() {
        val isAll = checkIsSelectAll()
        if (isAll) {//全选，则反向全不选
            for (i in 0..childCount - 1) {
                val itemCollectView = getChildAt(i)
                if (itemCollectView != null && itemCollectView is ItemCollectView) {
                    itemCollectView.setSelect(false)
                }
            }
        } else {
            for (i in 0..childCount - 1) {
                val itemCollectView = getChildAt(i)
                if (itemCollectView != null && itemCollectView is ItemCollectView) {
                    //没有被选择的，设置被选择
                    if (!itemCollectView.getSelectStatu()) {
                        itemCollectView.setSelect(true)
                    }
                }
            }
        }

    }

    private fun checkIsSelectAll(): Boolean {
        for (i in 0..childCount - 1) {
            val itemCollectView = getChildAt(i)
            if (itemCollectView != null && itemCollectView is ItemCollectView) {
                if (!itemCollectView.getSelectStatu()) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 删除
     */
    fun delete() {
        for (i in 0..childCount - 1) {
            //如果有被选中的就交给帮助类删除
            val itemCollectView = getChildAt(i)
            if (itemCollectView != null && itemCollectView is ItemCollectView) {
                if (itemCollectView.getSelectStatu()) {
                    itemTouchStatus?.onItemRemove(getChildViewHolder(itemCollectView))
                }
            }
        }
    }

}