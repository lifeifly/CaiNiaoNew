package com.example.skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Constructor
import java.util.concurrent.CopyOnWriteArrayList

class SkinFactory : LayoutInflater.Factory2 {
    //需要换肤的容器
    private val mCollectedViews = CopyOnWriteArrayList<SkinView>()


    companion object {

        private val sClassPrefixList = arrayOf(
            "android.widget.",
            "android.view.",
            "android.webkit."
        )
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        var view: View? = null
        //实例化控件
        if (name.contains(".")) {
            //带包名的
            view = onCreateView(name, context, attrs)
        } else {
            //不带包名,系统控件
            for (s in sClassPrefixList){
                val viewName = s + name
                view = onCreateView(viewName, context, attrs)
                if (view!=null)break
            }
        }
        //收集需要换肤的控件
        collectNeedSkinView(view, attrs, name)
        return view
    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view: View? = null
        try {//反射实例化
            //加载类对象
            val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
            //获取构造方法,第二个
            val constructor = clazz.getConstructor(Context::class.java, AttributeSet::class.java)
            view = constructor.newInstance(context, attrs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    /**
     * 根据某种规则收集需要换肤的容器
     */
    private fun collectNeedSkinView(view: View?, attrs: AttributeSet, name: String) {
        val skinItems = mutableListOf<SkinItem>()
        //遍历控件的属性集合
        for (i in 0..attrs.attributeCount - 1) {

            //获取属性名字
            val attrName = attrs.getAttributeName(i)
            if (view is ViewStub||view is androidx.appcompat.widget.SearchView.SearchAutoComplete){
                continue
            }

            //获取资源的属性id
            if (attrName.contains("background")
                || attrName.contains("textColor")
                || attrName.contains("src")||attrName.contains("itemTextColor")||attrName.contains("itemIconTint"))
             {
                //获取属性值，可以是资源ID，也可以是颜色值,
                val attrValue = attrs.getAttributeValue(i)


                //必须是@开头的
                if (attrValue.contains("@")) {
                    //去掉@
                    val resId = attrValue.substring(1).toInt()
                    //获取属性值的的名称和属性值的类型
                    val resName = view?.context?.resources?.getResourceEntryName(resId)
                    val resType = view?.context?.resources?.getResourceTypeName(resId)

                    //要是blue1_style才能加入
                    if (resName.equals("blue1_style")){
                        Log.d("TAG15", "collectNeedSkinView: "+view!!::class.java.name)
                        val skinItem = SkinItem(attrName, resType, resName, resId)
                        skinItems.add(skinItem)
                    }
                }
            }
        }
        if (view!=null&&skinItems.size>0){//防止一个view对应的空属性加入集合中，造成内存泄漏
            mCollectedViews.add(SkinView(view,skinItems))
        }
    }

    fun getSkinViews():CopyOnWriteArrayList<SkinView>{
        return mCollectedViews
    }

}