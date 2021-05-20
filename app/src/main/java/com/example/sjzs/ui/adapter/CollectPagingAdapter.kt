package com.example.sjzs.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.SearchBean
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.click.IItemClick
import com.example.sjzs.ui.recyclerview.ItemTouchStatus

class CollectPagingAdapter : PagingDataAdapter<CollectBean, CollectRvItemViewHolder>(diff),
    IItemClick<CollectBean> {
    companion object {
        private val diff = object : DiffUtil.ItemCallback<CollectBean>() {
            override fun areItemsTheSame(oldItem: CollectBean, newItem: CollectBean): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areContentsTheSame(oldItem: CollectBean, newItem: CollectBean): Boolean {
                return oldItem.id.equals(newItem.id)
            }
        }
    }

    override fun onBindViewHolder(holder: CollectRvItemViewHolder, position: Int) {
        val collectBean = getItem(position)
        if (collectBean != null) {
            holder.bindTo(collectBean, this)
            if (mCollectLoadListener!=null){
                mCollectLoadListener?.loadData()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectRvItemViewHolder {
        return CollectRvItemViewHolder.instance(parent)
    }

    override fun onClick(view: View, t: CollectBean) {
        val intent = Intent()
        intent.putExtra("data", CollectBean(t.id, t.title, t.image, t.url, t.date))

        if (t.id.contains("ARTI")) {
            intent.setClass(view.context, ArticleActivity::class.java)
        } else if (t.id.contains("PHOA")) {
            intent.setClass(view.context, PhotoActivity::class.java)
        }
        view.context.startActivity(intent)
    }

    private var mCollectLoadListener:CollectLoadListener?=null

    fun setListener(collectLoadListener: CollectLoadListener){
        this.mCollectLoadListener=collectLoadListener
    }

    //回调加载成功
    interface CollectLoadListener{
        fun loadData()
    }

}