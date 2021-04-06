package com.example.sjzs.ui.recyclerview.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.LayoutRvSocietyBinding
import com.example.sjzs.model.bean.ListBean
import com.example.sjzs.ui.adapter.base.BaseViewHolder

class Adapter(private val context:Context):PagedListAdapter<ListBean,BaseViewHolder>(
    DIFF_CALLBACK
) {

    companion object{
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ListBean> = object : DiffUtil.ItemCallback<ListBean>() {
            override fun areItemsTheSame(oldItem: ListBean, newItem: ListBean): Boolean {
                Log.d("TAG12", "areItemsTheSame: ")

                    return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(oldItem: ListBean, newItem: ListBean): Boolean {
                Log.d("TAG12", "areItemsTheSame: 1")

                    return (oldItem as ListBean) == (newItem as ListBean)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<LayoutRvSocietyBinding>(
            LayoutInflater.from(context),
            R.layout.layout_rv_society,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.binding.setVariable(BR.listbean,getItem(position))
        holder.binding.executePendingBindings()//防止闪烁
    }
}