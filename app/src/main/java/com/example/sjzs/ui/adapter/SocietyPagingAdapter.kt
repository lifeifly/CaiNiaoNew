package com.example.sjzs.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.ChinaList
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.SocietyList
import com.example.sjzs.model.bean.WorldList
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity

import com.example.sjzs.ui.adapter.base.BaseBindingAdapter
import com.example.sjzs.ui.click.IItemClick

class SocietyPagingAdapter(
    private val context: Context
) : BaseBindingAdapter<SocietyList,
    RecyclerView.ViewHolder>(context),
    IItemClick<SocietyList> {

    override fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SocietyRvItemViewHolder.instance(parent)
    }

    override fun onBindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val societyList = getItem(position) ?: return
        (holder as SocietyRvItemViewHolder).bindTo(societyList, this)
    }

    /**
     * 处理点击跳转的逻辑
     */
    override fun onClick(view: View, t: SocietyList) {
        val intent = Intent()
        intent.putExtra("data", CollectBean(t.id, t.title, t.image, t.url,0))

        if (t.id.contains("ARTI")) {
            intent.setClass(view.context, ArticleActivity::class.java)
        } else if (t.id.contains("PHOA")) {
            intent.setClass(view.context, PhotoActivity::class.java)
        }
        view.context.startActivity(intent)
    }

}