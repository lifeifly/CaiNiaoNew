package com.example.sjzs.ui.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.sjzs.R
import kotlinx.android.synthetic.main.activity_main.view.*

class StatusBarActivityLifecycle(private val activity: Activity):LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
     fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
     fun onStart() {
        Log.d("TAG", "onStart: ")
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val statusBarView=activity.findViewById<View>(R.id.statusBarView)
        val layoutParams = statusBarView.layoutParams
        layoutParams.height=getStatusBarHeight()
        statusBarView.layoutParams=layoutParams
    }
    /**
     * 获取状态栏高度
     */
    private fun getStatusBarHeight():Int{
        var result=0
        //获取状态栏高度的资源id
        val resId=activity.resources.getIdentifier("status_bar_height","dimen","android")
        if (resId>0){
            result=activity.resources.getDimensionPixelSize(resId)
        }
        return result
    }

}