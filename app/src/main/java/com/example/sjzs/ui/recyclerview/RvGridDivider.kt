package com.example.sjzs.ui.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sjzs.ui.adapter.FooterViewHolder
import com.example.sjzs.ui.adapter.HeaderViewHolder

class RvGridDivider(private val space:Int):RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewholder=parent.getChildViewHolder(view)
        if (viewholder is HeaderViewHolder||viewholder is FooterViewHolder){
            return
        }

        val params=view.layoutParams
        if (params is StaggeredGridLayoutManager.LayoutParams){
            val index=params.spanIndex
            if (index==0||index==1){
                outRect.top=20
                outRect.bottom=10
            }else{
                outRect.top=10
                outRect.bottom=10
            }

            if (index%2==0){
                //低的一侧
                outRect.left=20
                outRect.right=10

            }else{
                //高的一侧
                outRect.left=10
                outRect.right=20
            }
        }
    }
}