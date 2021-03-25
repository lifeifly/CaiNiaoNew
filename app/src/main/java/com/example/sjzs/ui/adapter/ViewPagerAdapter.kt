package com.example.sjzs.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sjzs.ui.fragment.bean.FragmentInfo
import com.example.sjzs.ui.fragment.ChinaFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private var mFragments = mutableListOf<FragmentInfo>()

    init {
        initFragments()
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position].fragmentClass.newInstance() as Fragment
    }

    override fun getCount(): Int {
       return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragments[position].title
    }

    private fun initFragments() {
        mFragments.add(FragmentInfo("国内",ChinaFragment::class.java))

    }
}