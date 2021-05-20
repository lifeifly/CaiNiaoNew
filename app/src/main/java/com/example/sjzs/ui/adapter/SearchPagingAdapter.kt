package com.example.sjzs.ui.adapter

import android.content.Context
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
import com.example.sjzs.ui.adapter.base.BaseBindingAdapter
import com.example.sjzs.ui.click.IItemClick

class SearchPagingAdapter:PagingDataAdapter<SearchBean,SearchRvItemViewHolder>
    (diff),IItemClick<SearchBean> {



    override fun onClick(view: View, t: SearchBean) {
        val intent = Intent()
        intent.putExtra("data", CollectBean(t.id, t.title, t.image, t.url, 0))

        if (t.id.contains("ARTI")) {
            intent.setClass(view.context, ArticleActivity::class.java)
        } else if (t.id.contains("PHOA")) {
            intent.setClass(view.context, PhotoActivity::class.java)
        }
        view.context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: SearchRvItemViewHolder, position: Int) {
        val searchBean=getItem(position)
        if (searchBean != null) {
            holder.bindTo(searchBean,this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRvItemViewHolder {
        return SearchRvItemViewHolder.instance(parent)
    }

    companion object{
        private val diff=object : DiffUtil.ItemCallback<SearchBean>(){
            override fun areItemsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areContentsTheSame(oldItem: SearchBean, newItem: SearchBean): Boolean {
                return oldItem.id.equals(newItem.id)
            }
        }
    }
}