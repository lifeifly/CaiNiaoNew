package com.example.sjzs.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skin.SkinFactory
import com.example.skin.SkinManager
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 换肤的顶级类
 */
abstract class BaseSkinActivity : AppCompatActivity() {
    private lateinit var mSkinFactory: SkinFactory
    private lateinit var mStatusBarActivityLifecycle: StatusBarActivityLifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        //为LayoutInflater设置Factory2，必须在super.onCreate之前
        mSkinFactory = SkinFactory()
        layoutInflater.factory2 = mSkinFactory
        val skinViews = SkinManager.getSkinViews(this)
        if (skinViews == null) {
            //将需要改变的view添加到SkinManager
            SkinManager.registerActivity(this, mSkinFactory.getSkinViews())
        }
        super.onCreate(savedInstanceState)
        mStatusBarActivityLifecycle=StatusBarActivityLifecycle(this)
        //实现沉浸式
        lifecycle.addObserver(mStatusBarActivityLifecycle)
    }


    override fun onResume() {
        super.onResume()
        skin()
    }

    /**
     * 换肤
     */
    private fun skin() {
        mSkinFactory.getSkinViews().forEach {
            it.skin(SkinManager.getResource())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SkinManager.deleteSkinViews(this)
        lifecycle.removeObserver(mStatusBarActivityLifecycle)
    }


}