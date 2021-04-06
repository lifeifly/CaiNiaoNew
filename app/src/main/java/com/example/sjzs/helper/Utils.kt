package com.example.sjzs.helper

import android.text.TextUtils
import com.example.sjzs.model.bean.VideoData

object Utils {

    fun getUrl(oldUrl: String): String {
        return oldUrl.replace("shtml", "xml")
    }

    //将指定的包含viedoId例如[!--begin:htmlVideoCode--]9eeb18fbd84044249a6f2c413b8c575e,0,1,16:9,newPlayer[!--end:htmlVideoCode--]的字符串，截取videoId并拼接成视频资源链接
    fun videoUrl(container: String): VideoData {
        val i = container.lastIndexOf(",");
        val j = container.indexOf("]");
        val substring = container.substring(j + 1, i);
        //9eeb18fbd84044249a6f2c413b8c575e,0,1,16:9
        val datas = substring.split(",")
        val id = datas[0]
        var widthRatio: Int? = null
        var heightRatio: Int? = null
        val ratios: List<String>?
        if (!TextUtils.isEmpty(datas[3])) {
            ratios = datas[3].split(":")
            widthRatio = ratios[0].toInt()
            heightRatio = ratios[1].toInt()
        }

        val videoData = VideoData(id, widthRatio, heightRatio)
        return videoData;
    }


}