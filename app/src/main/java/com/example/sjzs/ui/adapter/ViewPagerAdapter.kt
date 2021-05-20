package com.example.sjzs.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sjzs.ui.fragment.*
import com.example.sjzs.ui.fragment.bean.FragmentInfo

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private var mFragments = mutableListOf<FragmentInfo>()

    init {
        initFragments()
    }


    private fun initFragments() {
        mFragments.add(FragmentInfo("国内",ChinaFragment::class.java))
        mFragments.add(FragmentInfo("国际",WorldFragment::class.java))
        mFragments.add(FragmentInfo("社会",SocietyFragment::class.java))
        mFragments.add(FragmentInfo("法治",LawFragment::class.java))
        mFragments.add(FragmentInfo("文娱",EntertainmentFragment::class.java))
        mFragments.add(FragmentInfo("科技",TechnologyFragment::class.java))
        mFragments.add(FragmentInfo("生活",LifeFragment::class.java))
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragments[position].fragmentClass.newInstance() as Fragment
    }
}