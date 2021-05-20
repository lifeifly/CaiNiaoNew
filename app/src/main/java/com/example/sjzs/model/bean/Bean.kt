package com.example.sjzs.model.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 通用的界面列表数据
 */
data class ChinaData(
    val list: List<ChinaList>,
    val total: Int
)

data class WorldData(
    val list: List<WorldList>,
    val total: Int
)

data class SocietyData(
    val list: List<SocietyList>,
    val total: Int
)

data class LawData(
    val list: List<LawList>,
    val total: Int
)

data class EntertainmentData(
    val list: List<EntertainmentList>,
    val total: Int
)

data class TechData(
    val list: List<TechList>,
    val total: Int
)

data class LifeData(
    val list: List<LifeList>,
    val total: Int
)
data class VideoListData(
    val list: List<VideoList>,
    val total: Int
)

@Entity(tableName = "china_table")
data class ChinaList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "world_table")
data class WorldList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "society_table")
data class SocietyList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "law_table")
data class LawList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "entertainment_table")
data class EntertainmentList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "tech_table")
data class TechList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "life_table")
data class LifeList(
    @PrimaryKey var id: String,
    var brief: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var image: String,
    var keywords: String,
    var title: String,
    var url: String,
    var count: String,
    var ext_field: String,
    var image2: String,
    var image3: String
) : BaseObservable()

@Entity(tableName = "video_table")
data class VideoList(
    @PrimaryKey var id: String,
    var image2: String,
    @ColumnInfo(name = "date") var focus_date: String,
    var title: String,
    var desc: String,
    var keywords: String,
    var level: String,
    var page_id: String,
    var image: String,
    var url: String,
    var image3: String
) : BaseObservable()


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
data class VideoData(val id: String, val widthRatio: Int?, val heightRatio: Int?)

data class PhotoList(
    val rollData: List<RollData>
)

data class RollData(
    val brief: String,
    val catalog: String,
    val content: String,
    val dateTime: String,
    val description: String,
    val id: String,
    val image: String,
    val image1: String,
    val image2: String,
    val relatedNews1: String,
    val relatedNews2: String,
    val relatedNewsUrl1: String,
    val relatedNewsUrl2: String,
    val title: String,
    val type: String,
    val url: String
):BaseObservable()


data class SearchBean(val id:String,val title:String,val image: String,val url:String)

@Entity(tableName = "history_table")
data class SearchHistory(@PrimaryKey val keyword: String, val date:Long)

@Entity(tableName = "collect_table")
class CollectBean(@PrimaryKey val id: String,
                  val title: String?,
                  val image: String?,
                  val url: String?,
                  var date: Long
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(image)
        parcel.writeString(url)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CollectBean> {
        override fun createFromParcel(parcel: Parcel): CollectBean {
            return CollectBean(parcel)
        }

        override fun newArray(size: Int): Array<CollectBean?> {
            return arrayOfNulls(size)
        }
    }

}