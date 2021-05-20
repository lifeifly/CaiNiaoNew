package com.example.skin

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.util.Log
import com.example.video.skin.SkinResource
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 加载皮肤资源包
 * 1.获取资源包,创建资源对象，可以访问资源包的资源
 * 2.获取他里面的资源
 */
object SkinManager {

    //记录上次加载的resPath
    private var mLastResPath: String? = null

    //上下文
    private lateinit var context: Context

    //对应的资源对象
    private lateinit var mSkinResource: SkinResource

    //需要换的activity和对应的view集合
    private var mNeedChangeView = ConcurrentHashMap<Activity, CopyOnWriteArrayList<SkinView>>()

    fun init(context: Context) {
        this.context = context

    }

    //加载皮肤包资源,并换肤
    fun loadSkinApk(skinPath: String?) {
        //初始化资源
        mSkinResource = SkinResource(skinPath, context)

        //上次是默认样式，且skinpath为空说明不需要换肤,防止多次调用
        if (TextUtils.isEmpty(mLastResPath)) {
            //说明是默认样式
            if (!TextUtils.isEmpty(skinPath)) {
                mLastResPath = skinPath
                //需要换肤
                skin(mSkinResource)
            }
        } else {
            //上次是其他样式
            if (!mLastResPath.equals(skinPath)) {
                //说明两次换肤路径不一致，需要换肤
                mLastResPath = skinPath
                skin(mSkinResource)
            }
        }
    }

    private fun skin(skinResource: SkinResource) {
        mNeedChangeView.entries.forEach {
            it.value.forEach {
                it.skin(skinResource)
            }
        }
    }

    fun getResource(): SkinResource {
        return mSkinResource
    }

    /**
     * 添加需要换肤的activity和对应的view
     */
    fun registerActivity(activity: Activity, changedViews: CopyOnWriteArrayList<SkinView>) {
        mNeedChangeView.put(activity, changedViews)
    }


    fun getSkinViews(activity: Activity): List<SkinView>? {
        return mNeedChangeView.get(activity)
    }

    fun deleteSkinViews(activity: Activity) {
        if (mNeedChangeView[activity] != null) {
            mNeedChangeView.remove(activity)
        }
    }

}