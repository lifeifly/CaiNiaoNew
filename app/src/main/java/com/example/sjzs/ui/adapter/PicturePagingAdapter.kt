package com.example.sjzs.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.*
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity

import com.example.sjzs.ui.adapter.base.BaseBindingAdapter
import com.example.sjzs.ui.click.IItemClick

class PicturePagingAdapter(
    private val context: Context
) : BaseBindingAdapter<RollData,
    RecyclerView.ViewHolder>(context),
    IItemClick<RollData> {

    override fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PictureRvItemViewHolder.instance(parent)
    }

    override fun onBindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val rollData = getItem(position) ?: return
        Log.d("TAG456", "load: 1"+rollData.content)
        (holder as PictureRvItemViewHolder).bindTo(rollData, this)
    }

    /**
     * 处理点击跳转的逻辑
     */
    override fun onClick(view: View, t: RollData) {
        val intent = Intent()
        intent.putExtra("data", CollectBean(t.id, t.title, t.image, t.url, 0))

        if (t.id.contains("ARTI")) {
            intent.setClass(view.context, ArticleActivity::class.java)
        } else if (t.id.contains("PHOA")) {
            intent.setClass(view.context, PhotoActivity::class.java)
        }
        view.context.startActivity(intent)
    }

}