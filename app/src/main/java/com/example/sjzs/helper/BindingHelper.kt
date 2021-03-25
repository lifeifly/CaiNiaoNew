package com.example.sjzs.helper

import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.sjzs.R
import com.example.sjzs.model.bean.*

object BindingHelper {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context).load(imageUrl).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["data"])
    fun swipeArticle(container: LinearLayout, data: Node?) {
        if (data==null)return
        Log.d("TAG", data.toString())
        var childIndex = 1
        val context = container.context
        //将文本和图片数据显示出来

        var current:Node?=data
        while (current?.next!=null){
            when (current.next) {
                is Img -> {
                    val img=current.next as Img
                    //属于图片数据
                    val imageView =
                        ImageView(context)

                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (img.width!=null){
                        param.width= img.width
                    }
                    //设置居中显示
                    param.gravity =
                        Gravity.CENTER //必须要加上这句，setMargins才会起作用，而且此句还必须在setMargins下面
                    imageView.layoutParams = param
                    //利用Picasso框架加载网络图片
                    Glide.with(context).load(img.src).into(imageView)
                    container.addView(imageView, childIndex++)
                }
                is Strong -> {
                    val strong=current.next as Strong
                    val textView = TextView(context)
                    textView.text = strong.strong+"\n"
                    textView.textSize = 20f
                    //设置加粗
                    textView.setTypeface(Typeface.DEFAULT_BOLD)
                    //设置黑色
                    textView.setTextColor(context.resources.getColor(R.color.def_text_black))
                    //添加到ll中
                    container.addView(textView, childIndex++)
                }
                is  Text-> {
                    val text=current.next as Text
                    val textView = TextView(context)
                    //设置黑色
                    textView.setTextColor(context.resources.getColor(R.color.def_text_black))
                    textView.setText(text.text+"\n")
                    textView.textSize = 15f
                    //添加到ll中
                    container.addView(textView, childIndex++)
                }
                is HeadText -> {
                    val headText=current.next as HeadText

                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val tvImageDes = TextView(context)
                    tvImageDes.layoutParams=param
                    tvImageDes.gravity=Gravity.CENTER_HORIZONTAL
                    tvImageDes.text = headText.headText+"\n"
                    tvImageDes.setTypeface(Typeface.DEFAULT_BOLD)
                    tvImageDes.textSize = 17f

                    container.addView(tvImageDes, childIndex++)
                }
            }
            current=current.next
        }
    }



}
