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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentLifeBinding
import com.example.sjzs.ui.adapter.LifePagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.LifeDataViewModel
import kotlinx.coroutines.launch

class LifeFragment: AbsBaseLazyFragment() {
    private val mLifeDataVM:LifeDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(LifeDataViewModel::class.java)
    }
    private lateinit var mLifeFargmentBinding:FragmentLifeBinding
    private lateinit var mLifePagingAdapter:LifePagingAdapter
    override fun getLayoutRes(): Int {
        return R.layout.fragment_life
    }

    override fun initView(rootView: View) {
        mLifeFargmentBinding= FragmentLifeBinding.bind(rootView)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        mLifePagingAdapter= LifePagingAdapter(activity as Context)
        mLifeFargmentBinding.lifeRv.layoutManager=LinearLayoutManager(activity)
        mLifeFargmentBinding.lifeRv.addItemDecoration(RvDivider())
        mLifeFargmentBinding.lifeRv.adapter=mLifePagingAdapter

        mLifeDataVM.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                mLifePagingAdapter.submitData(it)
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