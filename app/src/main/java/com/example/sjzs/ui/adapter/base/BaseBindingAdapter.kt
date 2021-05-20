package com.example.sjzs.ui.adapter.base

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.ChinaList
import com.example.sjzs.ui.activity.ArticleActivity
import com.example.sjzs.ui.activity.PhotoActivity
import com.example.sjzs.ui.adapter.FooterViewHolder
import com.example.sjzs.ui.adapter.HeaderViewHolder
import com.example.sjzs.ui.click.IItemClick

abstract class BaseBindingAdapter<T : Any, VH : RecyclerView.ViewHolder>(context: Context) :
    PagingDataAdapter<T, VH>(getDiffCallback<T>()) {

    val mContext: Context = context

    //头部的ViewHolder
    private  var mHeaderViewHolder:HeaderViewHolder?=null
    //底部的ViewHolder
    private  var mFooterViewHolder: FooterViewHolder?=null
    companion object {
        //标记头部和底部类型
         const val ITEM_TYPE_HEADER = 0x111
         const val ITEM_TYPE_FOOTER = 0x222


        /**
         * 处理是否更新数据
         */
        fun <T> getDiffCallback(
            areItemsTheSame: (T, T) -> Boolean = { o, n -> o == n },
            areContentsTheSame: (T, T) -> Boolean = { o, n -> o == n }
        ): DiffUtil.ItemCallback<T> {
            val DIFF_CALLBACK: DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
                override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                    return areItemsTheSame(oldItem, newItem)
                }

                override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                    return areContentsTheSame(oldItem, newItem)
                }
            }
            return DIFF_CALLBACK
        }
    }
    /**
     * 开始刷新动画
     */
    fun startRefresh(){
        mHeaderViewHolder?.startRefresh()
    }

    /**
     * 刷新成功
     */
    fun showRefreshSuccess(){
        mHeaderViewHolder?.showSuccess()
    }
    /**
     * 刷新失败
     */
    fun showRefreshError(){
        mHeaderViewHolder?.showError()
    }

    /**
     * 重置状态
     */
    fun resetRefresh(){
        mHeaderViewHolder?.reset()
    }
    /**
     * 开始加载更多动画
     */
    fun startLoadMore(){
        mFooterViewHolder?.startLoadMore()
    }

    /**
     * 加载更多成功
     */
    fun showLoadMoreNoData(){
        mFooterViewHolder?.showNoMoreData()
    }
    /**
     * 加载更多失败
     */
    fun showLoadMoreError(){
        mFooterViewHolder?.showError()
    }

    /**
     * 重置加载更多
     */
    fun resetLoadMore(){
        mFooterViewHolder?.reset()
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            itemCount - 1 -> ITEM_TYPE_FOOTER
            else -> super.getItemViewType(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            ITEM_TYPE_HEADER -> {
                mHeaderViewHolder=HeaderViewHolder.instance(parent, { retry() })
                mHeaderViewHolder as VH
            }
            ITEM_TYPE_FOOTER -> {
                mFooterViewHolder=FooterViewHolder.instance(parent, { retry() })
                mFooterViewHolder as VH
            }
            else -> onCreateVH(parent, viewType)
        }
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
            }
            is FooterViewHolder -> {
            }
            else -> {
                onBindVH(holder, position - 1)
            }
        }

    }

    /**
     * 代理系统的observer，但是位置+1，改变了系统的逻辑
     */
    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, 1))
    }

    abstract fun onBindVH(holder: VH, position: Int)

    abstract fun onCreateVH(parent: ViewGroup, viewType: Int): VH




    /**
     * 代理刷新逻辑
     */
    class AdapterDataObserverProxy(
        private val represented: RecyclerView.AdapterDataObserver,
        private val headerSize: Int
    ) : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            represented.onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            represented.onItemRangeRemoved(positionStart + headerSize, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            represented.onItemRangeMoved(
                fromPosition + headerSize,
                toPosition + headerSize,
                itemCount
            )
        }

        override fun onStateRestorationPolicyChanged() {
            represented.onStateRestorationPolicyChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            represented.onItemRangeInserted(positionStart + headerSize, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            represented.onItemRangeChanged(positionStart + headerSize, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            represented.onItemRangeChanged(positionStart + headerSize, itemCount, payload)
        }
    }
}