package com.example.sjzs.ui.adapter.base

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.click.IItemClick

abstract class BaseBindingAdapter<T, VH : RecyclerView.ViewHolder>(context: Context) :
    PagedListAdapter<T, VH>(getDiffCallback<T>()),
    IItemClick<ListBean> {

    val mContext: Context = context
    companion object {
        /**
         * 处理是否更新数据
         */
        fun <T> getDiffCallback(): DiffUtil.ItemCallback<T> {
            val DIFF_CALLBACK: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    Log.d("TAG12", "areItemsTheSame: ")
                    if (oldItem is ListBean && newItem is ListBean) {
                        return (oldItem as ListBean).id == (newItem as ListBean).id
                    }
                    return false
                }

                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    Log.d("TAG12", "areItemsTheSame: 1")
                    if (oldItem is ListBean && newItem is ListBean) {
                        return (oldItem as ListBean) == (newItem as ListBean)
                    }
                    return false
                }
            }
            return DIFF_CALLBACK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateVH(parent, viewType)
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindVH(holder, position)
    }

    abstract fun onBindVH(holder: VH, position: Int)

    abstract fun onCreateVH(parent: ViewGroup, viewType: Int): VH


    /**
     * 处理点击跳转的逻辑
     */
    override fun onClick(view: View, t: ListBean) {
        val intent: Intent = Intent()
        intent.putExtra("data", t.url)
        if (t.id.contains("ARTI")) {
            intent.setClass(view.context, ArticleActivity::class.java)
        } else if (t.id.contains("PHOA")) {
            intent.setClass(view.context, PhotoActivity::class.java)
        }
        view.context.startActivity(intent)
    }

}