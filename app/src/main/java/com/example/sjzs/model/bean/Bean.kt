package com.example.sjzs.model.bean

import androidx.databinding.BaseObservable
import java.io.Serializable

/**
 * 通用的界面列表数据
 */
data class CommonData(
    val list: List<ListBean>,
    val total: Int
)

data class ListBean(
    val brief: String,
    val count: String,
    val ext_field: String,
    val focus_date: String,
    val id: String,
    val image: String,
    val image2: String,
    val image3: String,
    val keywords: String,
    val title: String,
    val url: String
) : BaseObservable(), Serializable


//文章数据bean
data class ArticleBean(
    val title: String,
    val keyword: String,
    val source: String,
    val time: String,
    val des: String,
    val videoData: VideoData?,
    val editor: String,
    val data: Node
)

open class Node {
    var next: Node? = null
    private var lastNode: Node? = null
    fun setNexNode(node: Node) {
        if (lastNode == null) {
            next = node
            lastNode = next
        } else {
            lastNode?.next = node
            lastNode = lastNode?.next
        }
    }
}

data class Strong(val strong: String) : Node()
data class Img(val src: String, val width: Int?) : Node()
data class Text(val text: String) : Node()
data class HeadText(val headText: String) : Node()

/**
 * 通用的广告数据类
 */
data class CommomBanner(
    val href: String,
    val srcHref: String,
    val title: String,
    val text: String
) : BaseObservable()

/**
 * 视频id和宽高比封装bean
 */
data class VideoData(val id: String, val widthRatio: Int?,val heightRatio: Int?)

