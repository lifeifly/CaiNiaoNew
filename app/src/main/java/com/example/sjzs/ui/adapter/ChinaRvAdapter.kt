package com.example.sjzs.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.LayoutRvSocietyBinding
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.adapter.base.BaseBindingAdapter
import com.example.sjzs.ui.adapter.base.BaseViewHolder

class ChinaRvAdapter(context: Context) : BaseBindingAdapter<ListBean, BaseViewHolder>(context) {

    override fun onCreateVH(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<LayoutRvSocietyBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_rv_society,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindVH(holder: BaseViewHolder, position: Int) {
        Log.d("LIVE1", ": "+itemCount)
        holder.binding.setVariable(BR.listbean,getItem(position))
        (holder.binding as LayoutRvSocietyBinding).itemClick=this
        holder.binding.executePendingBindings()//防止闪烁
    }
}