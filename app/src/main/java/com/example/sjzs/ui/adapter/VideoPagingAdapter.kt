package com.example.sjzs.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sjzs.model.bean.ChinaList
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.model.bean.VideoList
import com.example.sjzs.model.bean.WorldList
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity

import com.example.sjzs.ui.adapter.base.BaseBindingAdapter
import com.example.sjzs.ui.click.IItemClick

class VideoPagingAdapter(
    private val context: Context
) : BaseBindingAdapter<VideoList, RecyclerView.ViewHolder>(context),IItemClick<VideoList> {

    override fun onCreateVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoRvItemViewHolder.instance(parent)
    }

    override fun onBindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val videoList = getItem(position) ?: return
        Log.d("ONBIND", "onBindVH: "+videoList.id)
        (holder as VideoRvItemViewHolder).bindTo(videoList, this)
    }

    /**
     * 在gridlayoutmanager，控制头部和底部占几个单元格
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager=recyclerView.layoutManager
        if (layoutManager is GridLayoutManager){
            //获取设置的spanSize
            layoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (getItemViewType(position)== ITEM_TYPE_HEADER||getItemViewType(position)== ITEM_TYPE_FOOTER){
                        //当前式头部或底部，设置item的跨度为spanCount
                        layoutManager.spanCount
                    }else{
                        //其他的设置为1
                        1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        //获取holder的layoutparans
        val params=holder.itemView.layoutParams
        if (params!=null&&params is StaggeredGridLayoutManager.LayoutParams&&(holder is HeaderViewHolder||holder is FooterViewHolder)){
            val p=params
            p.isFullSpan=true
        }
    }
    /**
     * 处理点击跳转的逻辑
     */
    override fun onClick(view: View, t: VideoList) {
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