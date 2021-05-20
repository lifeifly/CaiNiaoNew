package com.example.sjzs.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentSocietyBinding
import com.example.sjzs.databinding.FragmentWorldBinding
import com.example.sjzs.model.bean.SocietyList
import com.example.sjzs.ui.adapter.SocietyPagingAdapter
import com.example.sjzs.ui.adapter.WorldPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.SocietyDataViewModel
import kotlinx.coroutines.launch

class SocietyFragment: AbsBaseLazyFragment() {
    private val mSocietyDataVM:SocietyDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(SocietyDataViewModel::class.java)
    }
    private lateinit var mSocietyBinding: FragmentSocietyBinding
    private lateinit var societyPagingAdapter:SocietyPagingAdapter
    override fun getLayoutRes(): Int {
        return R.layout.fragment_society
    }

    override fun initView(rootView: View) {
        mSocietyBinding= FragmentSocietyBinding.bind(rootView)
        setRecyclerViewData()
    }


    /**
     * 监听recyclerview的数据变化
     */
    fun setRecyclerViewData() {
        //设置头部底部刷新
        societyPagingAdapter = SocietyPagingAdapter(
            activity as Context
        )

        mSocietyBinding.societyRv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mSocietyBinding.societyRv.adapter = societyPagingAdapter

        //添加分割线
        mSocietyBinding.societyRv.addItemDecoration(RvDivider())
        //监听数据发生变化，刷新RecyclerView
        mSocietyDataVM.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                societyPagingAdapter.submitData(it)
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