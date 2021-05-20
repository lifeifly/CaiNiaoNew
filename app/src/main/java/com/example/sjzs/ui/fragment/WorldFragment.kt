package com.example.sjzs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentWorldBinding
import com.example.sjzs.ui.adapter.ChinaPagingAdapter
import com.example.sjzs.ui.adapter.WorldPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.ChinaDataViewModel
import com.example.sjzs.viewmodel.ChinaRequestViewModel
import com.example.sjzs.viewmodel.WorldDataViewModel
import kotlinx.coroutines.launch

class WorldFragment : AbsBaseLazyFragment() {
    private lateinit var worldPagingAdapter: WorldPagingAdapter
    private lateinit var bind: FragmentWorldBinding
    private val dataViewModel: WorldDataViewModel
            by lazy {
                ViewModelProvider(
                    this,
                    ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
                ).get(
                    WorldDataViewModel::class.java
                )
            }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_world
    }




    override fun initView(rootView: View) {
        bind = FragmentWorldBinding.bind(rootView)
        setRecyclerViewData()
    }

    /**
     * 监听recyclerview的数据变化
     */
    fun setRecyclerViewData() {
        //设置头部底部刷新
        worldPagingAdapter = WorldPagingAdapter(
            activity as Context
        )

        bind.worldRv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bind.worldRv.adapter = worldPagingAdapter

        //添加分割线
        bind.worldRv.addItemDecoration(RvDivider())
        //监听数据发生变化，刷新RecyclerView
        dataViewModel.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                worldPagingAdapter.submitData(it)
            }
        })
    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {
    }

    override fun complete() {

    }

    override fun showError(text: String) {

    }


}