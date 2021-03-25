package com.example.sjzs.ui.adapter.base

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.click.IItemClick

abstract class BaseBindingAdapter<T, VH : RecyclerView.ViewHolder>(context: Context) :
    RecyclerView.Adapter<VH>(),
    IItemClick<ListBean> {
    val mDataList: ArrayList<T> = ArrayList()
    val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateVH(parent,viewType)
    }



    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindVH(holder,position)
    }

    abstract fun onBindVH(holder:VH, position: Int)

    abstract fun onCreateVH(parent: ViewGroup, viewType: Int): VH

    fun refreshUI(ts:List<T>){
        mDataList.clear()
        mDataList.addAll(ts)
        notifyDataSetChanged()
    }

    /**
     * 处理点击跳转的逻辑
     */
    override fun onClick(view: View, t: ListBean) {
        val intent: Intent=Intent()
        intent.putExtra("data",t.url)
        if (t.id.contains("ARTI")){
            intent.setClass(view.context,ArticleActivity::class.java)
        }else if (t.id.contains("PHOA")){
          intent.setClass(view.context,PhotoActivity::class.java)
    }
        view.context.startActivity(intent)
    }
}