package com.example.sjzs.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.sjzs.R
import com.example.sjzs.ui.adapter.ViewPagerAdapter
import com.example.sjzs.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDrawerLayout()
        initTabLayout()
    }



    private fun initTabLayout() {
        val viewPagerAdapter=ViewPagerAdapter(supportFragmentManager)
        mViewPager.adapter=viewPagerAdapter
        //关联TabLayout
        tab_layout.setupWithViewPager(mViewPager)

    }

    private fun initDrawerLayout(){
        //设置menu的点击事件
        navigation_view.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {

            }
            false
        }
        //加载菜单
        tool_bar.inflateMenu(R.menu.main_toolbar_menu)
        //与DrawerLayout整合
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            tool_bar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        actionBarDrawerToggle.syncState()
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun requestData() {

    }

    override fun complete() {

    }
}