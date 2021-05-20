package com.example.sjzs.model.room

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.sjzs.model.bean.CollectBean
import com.example.sjzs.ui.adapter.CollectRvItemViewHolder
import com.example.sjzs.ui.recyclerview.DataIsNullListener
import com.example.sjzs.ui.recyclerview.ItemTouchStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CollectDataBaseHelper(
    private val context: Context,
    private val adapter: RecyclerView.Adapter<CollectRvItemViewHolder>,
    private val mDataIsNullListener: DataIsNullListener
) : ItemTouchStatus {
    private val mDatabase = ListBeanDatabase.getDatabase(context)

    private val mCollectDao = mDatabase.collectDao()

    override fun onItemMove(
        recyclerView: RecyclerView,
        start: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        //交换数据库中的时间
        if (start is CollectRvItemViewHolder && target is CollectRvItemViewHolder && start.collectBean != null && target.collectBean != null) {
            val startDate = start.collectBean?.date
            start.collectBean?.date = target.collectBean?.date!!
            target.collectBean?.date = startDate!!
            GlobalScope.launch {
                mCollectDao.update(start.collectBean!!, target.collectBean!!)
            }
        }

        adapter.notifyItemMoved(start.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }


    override fun onItemRemove(target: RecyclerView.ViewHolder) {

        if (target is CollectRvItemViewHolder && target.collectBean != null) {
            GlobalScope.launch(Dispatchers.Main) {
                mCollectDao.deleteByCollectBean(target.collectBean!!)
                val data: List<CollectBean> = mCollectDao.queryAll()
                Log.d("TAG", "loadData: " + data.size)
                if (data.size == 0) {
                    //数据删除后如果没有数据了则回调出去
                    mDataIsNullListener.dataIsNull()
                }
            }
        }
        adapter.notifyItemRemoved(target.bindingAdapterPosition)

    }


}