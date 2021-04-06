package com.example.video.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper


object ToolUtils {
    /**
     * 一小时的秒数
     */
    private val HOUR_SECOND = 60 * 60 * 1000;

    /**
     * 一分钟的秒数
     */
    private val MINUTE_SECOND = 60 * 1000;

    /**
     * 改变int类型时间的格式
     */
    fun makeTimeString(millsecond: Int): String {
        var total = millsecond
        if (total <= 0) {
            return "00:00"
        }
        val sb = StringBuilder()
        val hours: Int = millsecond / HOUR_SECOND
        if (hours > 0) {
            total -= hours * HOUR_SECOND
            if (hours >= 10) {
                sb.append("$hours:")
            } else {
                sb.append("0$hours:")
            }
        }
        val minute: Int = total / MINUTE_SECOND
        if (minute > 0) {
            total -= minute * MINUTE_SECOND
            if (minute >= 10) {
                sb.append("$minute:")
            } else {
                sb.append("0$minute:")
            }
        } else {
            sb.append("00:")
        }

        if (total > 0) {
            val sec: Int = total / 1000
            if (sec >= 10) {
                sb.append("${sec}")
            } else {
                sb.append("0${sec}")
            }
        } else {
            sb.append("00")
        }

        return sb.toString()
    }

    /**
     *获取对应的AppcompatActivity
     */
    fun getAppCompatActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompatActivity(context.getBaseContext())
        }
        return null
    }

    /**
     * 获取对应的activity
     */
    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * 隐藏状态栏、ActionBae
     */
    fun hide(context: Context?) {
        if (context == null) return
        val ab = getAppCompatActivity(context)?.supportActionBar
        ab?.hide()
    }

    /**
     * 显示ActionBar
     */
    fun show(context: Context?) {
        if (context == null) return
        val ab = getAppCompatActivity(context)?.supportActionBar
        ab?.show()
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar(context: Context?) {
        if (context == null) return
        scanForActivity(context)?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
    /**
     * 显示状态栏
     */
    fun showStatusBar(context: Context?) {
        if (context == null) return
        scanForActivity(context)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return width of the screen.
     */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return heiht of the screen.
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

}