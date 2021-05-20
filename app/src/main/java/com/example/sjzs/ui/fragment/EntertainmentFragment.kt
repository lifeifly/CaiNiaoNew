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
import com.example.sjzs.databinding.FragmentEntrtainmentBinding
import com.example.sjzs.ui.adapter.EnterPagingAdapter
import com.example.sjzs.ui.base.AbsBaseFragment
import com.example.sjzs.ui.base.AbsBaseLazyFragment
import com.example.sjzs.ui.recyclerview.RvDivider
import com.example.sjzs.viewmodel.EntertainmentDataViewModel
import kotlinx.coroutines.launch

class EntertainmentFragment: AbsBaseLazyFragment() {
    private lateinit var mEnterBinding:FragmentEntrtainmentBinding
    private lateinit var mEnterPagingAdapter:EnterPagingAdapter
    private val mEnterDataVM:EntertainmentDataViewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity?.application!!))
            .get(EntertainmentDataViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return  R.layout.fragment_entrtainment
    }

    override fun initView(rootView: View) {
        mEnterBinding= FragmentEntrtainmentBinding.bind(rootView)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        mEnterPagingAdapter= EnterPagingAdapter(activity as Context)
        mEnterBinding.enterRv.layoutManager=LinearLayoutManager(activity)
        mEnterBinding.enterRv.addItemDecoration(RvDivider())
        mEnterBinding.enterRv.adapter=mEnterPagingAdapter
        mEnterDataVM.listData.observe(this, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                mEnterPagingAdapter.submitData(it)
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