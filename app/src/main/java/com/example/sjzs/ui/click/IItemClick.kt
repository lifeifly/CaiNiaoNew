package com.example.sjzs.ui.click

import android.view.View

/**
 * RecyclerView点击事件的接口
 */
interface IItemClick<T> {

    fun onClick(view: View, t: T)
}