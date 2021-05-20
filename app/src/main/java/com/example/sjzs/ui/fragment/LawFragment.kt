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
import com.example.sjzs.databinding.FragmentLawBinding
import com.example.sjzs.ui.adapter.LawPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.LawDataViewModel
import kotlinx.coroutines.launch

class LawFragment : AbsBaseLazyFragment() {
    private val mLawDataVM: LawDataViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(LawDataViewModel::class.java)
    }

    private lateinit var mLawFragmentBinding: FragmentLawBinding
    private lateinit var mLawPagingAdapter: LawPagingAdapter

    override fun getLayoutRes(): Int {
        return R.layout.fragment_law
    }

    override fun initView(rootView: View) {
        mLawFragmentBinding = FragmentLawBinding.bind(rootView)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        mLawPagingAdapter = LawPagingAdapter(activity as Context)
        //添加分割线
        mLawFragmentBinding.lawRv.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mLawFragmentBinding.lawRv.addItemDecoration(RvDivider())
        mLawFragmentBinding.lawRv.adapter = mLawPagingAdapter
        mLawDataVM.listData.observe(this, Observer {
            lifecycleScope.launch {
                mLawPagingAdapter.submitData(it)
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