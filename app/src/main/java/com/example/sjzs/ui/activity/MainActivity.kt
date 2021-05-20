package com.example.sjzs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sjzs.R
import com.example.sjzs.ui.base.BaseSkinActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseSkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDrawerLayout()
        initNavigation()
    }

    private fun initNavigation() {
        //获取fragment容器
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //获取navControoller
        val navController = navHostFragment.navController
        //将fragment和bottomnavigationview和navigation绑定
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }


    private fun initDrawerLayout() {
        //设置menu的点击事件
        navigation_view.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.switch_skin -> {
                    startActivity(Intent(this, SkinActivity::class.java))
                }
                R.id.nav_download -> {
                    //前往收藏界面
                    startActivity(Intent(this,CollectActivity::class.java))
                    overridePendingTransition(R.anim.collect_slide_in_animation,R.anim.main_slide_out_animation)
                }
            }
            false
        }


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

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
    }


}