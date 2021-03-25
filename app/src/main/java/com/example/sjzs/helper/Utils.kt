package com.example.sjzs.helper

object Utils {

    fun getUrl(oldUrl: String): String {
        return oldUrl.replace("shtml", "xml")
    }

    //将指定的包含viedoId例如[!--begin:htmlVideoCode--]9eeb18fbd84044249a6f2c413b8c575e,0,1,16:9,newPlayer[!--end:htmlVideoCode--]的字符串，截取videoId并拼接成视频资源链接
    fun videoUrl(container: String): String {
        val i = container.indexOf(",");
        val j = container.indexOf("]");
        val substring = container.substring(j + 1, i);
        val realVideoUrl = "https://newcntv.qcloudcdn.com/asp/hls/1200/0303000a/3/default/" + substring
        return realVideoUrl;
    }


}