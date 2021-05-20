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
import com.example.sjzs.databinding.FragmentTechnologyBinding
import com.example.sjzs.ui.adapter.TechPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.TechDataViewModel
import kotlinx.coroutines.launch

class TechnologyFragment: AbsBaseLazyFragment() {
    private lateinit var mTechPagingAdapter:TechPagingAdapter
    private val mTechDataVM:TechDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(TechDataViewModel::class.java)
    }
    private lateinit var mTechBinding:FragmentTechnologyBinding
    override fun getLayoutRes(): Int {
        return R.layout.fragment_technology
    }

    override fun initView(rootView: View) {
        mTechBinding= FragmentTechnologyBinding.bind(rootView)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        mTechPagingAdapter= TechPagingAdapter(activity as Context)
        mTechBinding.techRv.layoutManager=LinearLayoutManager(activity)
        mTechBinding.techRv.addItemDecoration(RvDivider())
        mTechBinding.techRv.adapter=mTechPagingAdapter
        mTechDataVM.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                mTechPagingAdapter.submitData(it)
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