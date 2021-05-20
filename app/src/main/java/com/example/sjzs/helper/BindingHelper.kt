package com.example.sjzs.helper

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.sjzs.R
import com.example.sjzs.model.bean.*
import com.example.video.imageswitcher.ImageSwitcher
import com.example.video.imageswitcher.PhotoListBean

import com.example.video.video.ui.VideoView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object BindingHelper {
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "listener"], requireAll = false)
    fun loadImage(view: ImageView, imageUrl: String?, listener: RequestListener<*>?) {
        imageUrl ?: return
        Glide.with(view.context)
            .load(if (imageUrl.length > 6) imageUrl else R.drawable.ic_error_outline_24)
            .listener(listener as RequestListener<Drawable>?).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = [ "time"])
    fun setTime(view: TextView, time:String) {
        //转化为时间
        val date=Date(time.toLong())
        val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd ")
        val timeStr=simpleDateFormat.format(date)
        view.setText(timeStr)
    }

    @JvmStatic
    @BindingAdapter(value = ["data"])
    fun swipeArticle(container: LinearLayout, data: Node?) {
        if (data == null) return
        var childIndex = 0
        val context = container.context
        //将文本和图片数据显示出来

        var current: Node? = data
        while (current?.next != null) {
            when (current.next) {
                is Img -> {
                    val img = current.next as Img
                    //属于图片数据
                    val imageView =
                        ImageView(context)
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    if (img.width != null) {
                        param.width = img.width
                    }
                    //设置居中显示
                    param.gravity =
                        Gravity.CENTER //必须要加上这句，setMargins才会起作用，而且此句还必须在setMargins下面
                    imageView.layoutParams = param
                    //利用Glide框架加载网络图片
                    Glide.with(context).load(img.src).into(imageView)
                    container.addView(imageView, childIndex++)
                }
                is Strong -> {
                    val strong = current.next as Strong
                    val textView = TextView(context)
                    textView.text = strong.strong + "\n"
                    textView.textSize = 20f
                    //设置加粗
                    textView.setTypeface(Typeface.DEFAULT_BOLD)
                    //设置黑色
                    textView.setTextColor(context.resources.getColor(R.color.def_text_black))
                    //添加到ll中
                    container.addView(textView, childIndex++)
                }
                is Text -> {
                    val text = current.next as Text
                    val textView = TextView(context)
                    //设置黑色
                    textView.setTextColor(context.resources.getColor(R.color.def_text_black))
                    textView.setText(text.text + "\n")
                    textView.textSize = 15f
                    //添加到ll中
                    container.addView(textView, childIndex++)
                }
                is HeadText -> {
                    val headText = current.next as HeadText

                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val tvImageDes = TextView(context)
                    tvImageDes.layoutParams = param
                    tvImageDes.gravity = Gravity.CENTER_HORIZONTAL
                    tvImageDes.text = headText.headText + "\n"
                    tvImageDes.setTypeface(Typeface.DEFAULT_BOLD)
                    tvImageDes.textSize = 17f
                    container.addView(tvImageDes, childIndex++)
                }
            }
            current = current.next
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["videoData"])
    fun setVideo(videoView: VideoView, videoData: VideoData?) {
        if (videoData != null) {
            val widthRatio = videoData.widthRatio
            val heightRatio = videoData.heightRatio
            val videoId = videoData.id
            //存储路径
            val path = videoView.context.getExternalFilesDir(null)?.absolutePath + File.separator
            //设置videoview的宽高比
            if (widthRatio != null && heightRatio != null) {
                videoView.setWidthAndHeight(widthRatio, heightRatio)
            } else {
                videoView.setWidthAndHeight(16, 9)
            }
            //开始下载播放视频
            videoView.downloadAndMerge(videoId, path)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["photolist"])
    fun setPhotoList(imageSwitcher: ImageSwitcher, photoLists: MutableList<PhotoListBean>?) {
        if (photoLists != null) {
            imageSwitcher.setPhotoList(photoLists)
        }
    }

}
