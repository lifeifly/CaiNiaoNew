package com.example.video.skin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.skin.SkinManager

class SkinResource(private val resPath:String?,private val context: Context) {
    //资源包的资源对象
    var mResources: Resources?=null

    //资源包的包名
    lateinit var mPackageName: String

    /**
     * 加载资源包
     * @param resPath资源路径
     */
    init {
        if (!TextUtils.isEmpty(resPath)){
            Log.d("TAG123", ":1 ")
            //获取包管理器
            val packageManager = context.packageManager
            //获取资源包的包信息类
            val packageInfo =
                packageManager.getPackageArchiveInfo(resPath!!, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null) {
                mPackageName = packageInfo.packageName
            }

            val assetManager = AssetManager::class.java.newInstance()
            //通过反射获取addAssetPath方法
            val addAssetPathMethod =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPathMethod.invoke(assetManager, resPath)

            mResources = Resources(
                assetManager,
                context.resources?.displayMetrics,
                context.resources?.configuration
            )
        }else{
            Log.d("TAG123", ": ")
            //默认为系统的resource
            mResources=context.resources
            mPackageName=context.packageName
        }
    }


    /**
     * 去资源包张匹配颜色属性
     */
    fun getColor(resId:Int): Int {
        if (resourceIsNull())return resId
        //获取资源包中的资源的名字和类型
        val resName= context.resources?.getResourceEntryName(resId)
        val resType= context.resources?.getResourceTypeName(resId)
        //去资源包中匹配
        //因为多个包中的资源和类型一样，通过资源类型和名称获取对应资源的resId
        val identifier=
            mResources?.getIdentifier(resName,resType, mPackageName)
        if (identifier==0||identifier==null){
            //说明没有找到
            return resId
        }
        return mResources?.getColor(identifier)!!
    }
    /**
     * 去资源包张匹配图片属性
     */
    fun getDrawable(resId:Int): Drawable?{
        if (resourceIsNull())return ContextCompat.getDrawable(context,resId)
        //获取资源包中的资源的名字和类型
        val resName= context.resources?.getResourceEntryName(resId)
        val resType= context.resources?.getResourceTypeName(resId)
        //去资源包中匹配
        //因为多个包中的资源和类型一样，通过资源类型和名称获取对应资源的resId
        val identifier=
            mResources?.getIdentifier(resName,resType, mPackageName)
        if (identifier==0||identifier==null){
            //说明没有找到，返回自身的资源
            return ContextCompat.getDrawable(context,resId)
        }
        return mResources?.getDrawable(identifier)
    }

    fun resourceIsNull():Boolean{
        return mResources ==null
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        if (resourceIsNull())return ContextCompat.getColorStateList(context,resId)
        //获取资源包中的资源的名字和类型
        val resName= context.resources?.getResourceEntryName(resId)
        val resType= context.resources?.getResourceTypeName(resId)
        //去资源包中匹配
        //因为多个包中的资源和类型一样，通过资源类型和名称获取对应资源的resId
        val identifier=
            mResources?.getIdentifier(resName,resType, mPackageName)
        if (identifier==0||identifier==null){
            //说明没有找到，返回自身的资源
            return ContextCompat.getColorStateList(context,resId)
        }
        return mResources?.getColorStateList(identifier)
    }


}