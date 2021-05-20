package com.example.sjzs.ui.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class CustomItemTouchCallback(private val mItemTouchStatus:ItemTouchStatus):ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        //定义侧滑和拖动方向
        val dragFlag=ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag=ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlag,swipeFlag)
    }
    //拖动回调
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //交换在数据源中的位置
        return mItemTouchStatus.onItemMove(recyclerView,viewHolder,target)
    }
    //侧滑回调
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //从数据源中移除数据
        mItemTouchStatus.onItemRemove(viewHolder)

    }
}