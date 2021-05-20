package com.example.sjzs.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchStatus {
    fun onItemMove(recyclerView: RecyclerView, start:RecyclerView.ViewHolder, target:RecyclerView.ViewHolder): Boolean

    fun onItemRemove(target:RecyclerView.ViewHolder)
}