package com.example.sjzs.helper

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ShareUtils(private val context: Context) {



    fun shareToQQ(title:String,url:String){
        shareMsg("com.tencent.mobileqq",
            "com.tencent.mobileqq.activity.JumpActivity", "QQ", title,
            url)
    }
    fun shareToWeChat(title:String,url:String){
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信", title,
            url)
    }

    /**
     * @param packageName 跳转的应用包名
     * @param targetActivityName 跳转的activity类名
     * @param appName 跳转的应用名称
     *
     */
    private fun shareMsg(
        packageName: String,
        targetActivityName: String,
        appName: String,
        title: String,
        url: String
    ) {
        if (!packageName.isEmpty()&&!isAvaliable(context,packageName)){//判断是否存在应用
            Toast.makeText(context, "请先安装" + appName, Toast.LENGTH_SHORT)
                .show();
            return;
        }
        //指定分享到QQ好友
        val comp =
            ComponentName(packageName, targetActivityName)
        val intent= Intent("android.intent.action.SEND").apply {
            setType("text/plain")
            setPackage(packageName)
            putExtra(Intent.EXTRA_TEXT,url)
            putExtra(Intent.EXTRA_TITLE,title)
        }
        context.startActivity(intent)
    }

    private fun isAvaliable(context: Context, packageName: String): Boolean {
        val pm=context.packageManager

        val pInfos=pm.getInstalledPackages(0)
        pInfos.forEach {
            if (it.packageName.equals(packageName)){
                return true
            }
        }
        return false
    }
}