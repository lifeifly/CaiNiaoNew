package com.example.sjzs.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sjzs.R
import com.example.sjzs.databinding.FragmentHomeBinding
import com.example.sjzs.ui.activity.SearchActivity
import com.example.sjzs.ui.adapter.TrackIndicatorAdapter
import com.example.sjzs.ui.adapter.ViewPagerAdapter
import com.example.sjzs.ui.base.AbsBaseFragment

class HomeFragment:AbsBaseFragment() {
    private lateinit var mBinding:FragmentHomeBinding
    override fun getLayoutRes(): Int {
      return R.layout.fragment_home
    }

    override fun initView(rootView: View) {
        mBinding= FragmentHomeBinding.bind(rootView)
        mBinding.searchTv.setOnClickListener { v ->
            val intent= Intent(activity as Context, SearchActivity::class.java)
            (activity as Context).startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //关联TrackIndicatorView
        if (activity!=null){
            initTabLayout()
            mBinding.trackIndicator.setAdapter(TrackIndicatorAdapter(activity as Context),mBinding.mViewPager)
        }

    }


    override fun onDestroyView() {
        mBinding.mViewPager.removeAllViews()
        mBinding.coordinator.removeAllViews()
        super.onDestroyView()
        Log.d("HomeFragment", "onDestroyView: ")
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
    private fun initTabLayout() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        mBinding.mViewPager.adapter = viewPagerAdapter
    }


}