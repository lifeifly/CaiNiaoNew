package com.example.skin

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.video.skin.SkinResource
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

/**
 * 封装换肤的item
 * @param name 属性名字 textColor
 * @param typeName 属性值的类型 color drawable
 * @param entryName 属性值的名称 colorPrimary
 * @param resId 属性值的资源ID
 *
 */
data class SkinItem(val name: String, val typeName: String?, val entryName: String?, val resId: Int)

/**
 * 封装每个view的及对应需要换肤的Item
 */
class SkinView(private val view: View, private val items: List<SkinItem>) {
    /**
     * 换肤
     */
    fun skin(skinResource: SkinResource) {
        items.forEach {
            //需要设置的是background
            if (it.name == "background") {
                //1设置的是color颜色2设置的是图片
                if (it.typeName == "color") {
                    //将资源id传给SkinManager索引，找不到就直接设置之前的id
                    if (skinResource.resourceIsNull()) {
                        view.setBackgroundResource(skinResource.getColor(it.resId))
                    } else {
                        view.setBackgroundColor(skinResource.getColor(it.resId))
                    }
                } else if (it.typeName == "drawable" || it.typeName == "mipmap") {
                    view.background = skinResource.getDrawable(it.resId)
                }
            } else if (it.name == "src") {
                if (it.typeName == "drawable" || it.typeName == "mipmap") {
                    //将资源id传给SkinManager索引，找不到就直接设置之前的id
                    (view as ImageView).setImageDrawable(skinResource.getDrawable(it.resId))

                } else {
                    (view as ImageView).setImageResource(skinResource.getColor(it.resId))
                }
            } else if (it.name == "textColor") {
                (view as TextView).setTextColor(skinResource.getColor(it.resId))
            } else if (it.name == "itemIconTint") {
                (view as BottomNavigationView).itemTextColor =
                    skinResource.getColorStateList(it.resId)
            } else if (it.name == "itemTextColor") {
                (view as BottomNavigationView).itemIconTintList =
                    skinResource.getColorStateList(it.resId)
            }
        }
    }

    fun getView():View{
        return view
    }

    override fun toString(): String {
        return view.javaClass.name+items.toString()
    }
}