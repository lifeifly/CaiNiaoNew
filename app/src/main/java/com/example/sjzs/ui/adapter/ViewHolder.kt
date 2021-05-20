package com.example.sjzs.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.BR
import com.example.sjzs.R
import com.example.sjzs.databinding.*
import com.example.sjzs.model.bean.*
import com.example.sjzs.ui.click.IItemClick


class ChinaRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): ChinaRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemChinaRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_china_rv,
                parent,
                false
            )
            return ChinaRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(chinaList: ChinaList, itemClick: IItemClick<ChinaList>) {
        binding.setVariable(BR.listbean, chinaList)
        (binding as ItemChinaRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}

class WorldRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): WorldRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemWorldRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_world_rv,
                parent,
                false
            )
            return WorldRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(worldList: WorldList, itemClick: IItemClick<WorldList>) {
        binding.setVariable(BR.worldbean, worldList)
        (binding as ItemWorldRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}

class SocietyRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): SocietyRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemSocietyRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_society_rv,
                parent,
                false
            )
            return SocietyRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(societyList: SocietyList, itemClick: IItemClick<SocietyList>) {
        binding.setVariable(BR.societyList, societyList)
        (binding as ItemSocietyRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}
class LawRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): LawRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemLawRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_law_rv,
                parent,
                false
            )
            return LawRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(lawList: LawList, itemClick: IItemClick<LawList>) {
        binding.setVariable(BR.lawList, lawList)
        (binding as ItemLawRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}
class EnterRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): EnterRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemEnterRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_enter_rv,
                parent,
                false
            )
            return EnterRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(entertainmentList: EntertainmentList, itemClick: IItemClick<EntertainmentList>) {
        binding.setVariable(BR.enterList, entertainmentList)
        (binding as ItemEnterRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}
class TechRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): TechRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemTechRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_tech_rv,
                parent,
                false
            )
            return TechRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(techList: TechList, itemClick: IItemClick<TechList>) {
        binding.setVariable(BR.techList, techList)
        (binding as ItemTechRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}
class LifeRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): LifeRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemLifeRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_life_rv,
                parent,
                false
            )
            return LifeRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(lifeList: LifeList, itemClick: IItemClick<LifeList>) {
        binding.setVariable(BR.lifeList, lifeList)
        (binding as ItemLifeRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}

class PictureRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): PictureRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemPictureRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_picture_rv,
                parent,
                false
            )
            return PictureRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(rollData: RollData, itemClick: IItemClick<RollData>) {
        binding.setVariable(BR.pictureList, rollData)
        (binding as ItemPictureRvBinding).itemClick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}
class SearchRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): SearchRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemSearchRvLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_search_rv_layout,
                parent,
                false
            )
            return SearchRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(searchBean: SearchBean, itemClick: IItemClick<SearchBean>) {
        binding.setVariable(BR.searchList, searchBean)
        (binding as ItemSearchRvLayoutBinding).itemclick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}

class VideoRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup): VideoRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemVideoRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_video_rv,
                parent,
                false
            )
            return VideoRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(videoList: VideoList, itemClick: IItemClick<VideoList>) {
        binding.setVariable(BR.videolist, videoList)
        (binding as ItemVideoRvBinding).itemclick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}

class CollectRvItemViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    var collectBean:CollectBean?=null
    companion object {
        fun instance(parent: ViewGroup): CollectRvItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemCollectRvBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_collect_rv,
                parent,
                false
            )
            return CollectRvItemViewHolder(binding)
        }
    }

    /**
     * 绑定函数
     */
    fun bindTo(collectBean: CollectBean, itemClick: IItemClick<CollectBean>) {
        this.collectBean=collectBean
        binding.setVariable(BR.collectbean1, collectBean)
        (binding as ItemCollectRvBinding).itemclick = itemClick
        binding.executePendingBindings()//防止闪烁
    }
}


class FooterViewHolder(val binding: RvHeaderFooterLayoutBinding, private val retry: () -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun instance(parent: ViewGroup, retry: () -> Unit): FooterViewHolder {
            val binding = DataBindingUtil.inflate<RvHeaderFooterLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.rv_header_footer_layout,
                parent,
                false
            )
            //先将试图设为不可见，后续需要的时候会设置可见
            binding.tvFooter.visibility = View.GONE
            binding.refreshView.visibility = View.INVISIBLE
            return FooterViewHolder(binding, retry)
        }
    }


    /**
     * 开始刷新，显示动画
     */
    fun startLoadMore() {
        binding.refreshView.start()
        binding.refreshView.visibility = View.VISIBLE
        binding.tvFooter.visibility = View.GONE
        binding.root.isClickable = false
    }

    /**
     * 显示没数据
     */
    fun showNoMoreData() {
        binding.refreshView.stop()
        binding.tvFooter.visibility = View.VISIBLE
        binding.tvFooter.setText(R.string.load_no_more)
        binding.refreshView.visibility = View.INVISIBLE
        binding.root.isClickable = false
    }

    /**
     * 显示失败
     */
    fun showError() {
        binding.refreshView.stop()
        binding.refreshView.visibility = View.GONE

        binding.tvFooter.visibility = View.VISIBLE
        binding.tvFooter.setText(R.string.load_error)

        binding.root.isClickable = true
        binding.root.setOnClickListener { v ->
            retry.invoke()
        }

    }

    /**
     * 重置状态
     */
    fun reset() {
        binding.refreshView.visibility = View.VISIBLE
        binding.tvFooter.visibility = View.GONE
    }
}

class HeaderViewHolder(
    private val binding: RvHeaderFooterLayoutBinding,
    private val retry: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {


    companion object {
        fun instance(parent: ViewGroup, retry: () -> Unit): HeaderViewHolder {
            val binding = DataBindingUtil.inflate<RvHeaderFooterLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.rv_header_footer_layout,
                parent,
                false
            )

            return HeaderViewHolder(binding, retry)
        }
    }

    /**
     * 开始刷新，显示动画
     */
    fun startRefresh() {
        binding.refreshView.start()
        binding.refreshView.visibility = View.VISIBLE
        binding.tvFooter.visibility = View.GONE
        binding.root.isClickable = false
    }

    /**
     * 显示成功
     */
    fun showSuccess() {
        binding.refreshView.stop()
        binding.tvFooter.visibility = View.VISIBLE
        binding.tvFooter.setText(R.string.load_success)
        binding.refreshView.visibility = View.INVISIBLE
        binding.root.isClickable = false
    }

    /**
     * 显示失败
     */
    fun showError() {
        binding.refreshView.stop()
        binding.refreshView.visibility = View.GONE

        binding.tvFooter.visibility = View.VISIBLE
        binding.tvFooter.setText(R.string.load_error)

        binding.root.isClickable = true
        binding.root.setOnClickListener { v ->
            retry.invoke()
        }

    }

    /**
     * 重置状态
     */
    fun reset() {
        binding.refreshView.visibility = View.VISIBLE
        binding.tvFooter.visibility = View.GONE
    }
}