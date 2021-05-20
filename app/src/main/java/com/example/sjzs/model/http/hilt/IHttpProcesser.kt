package com.example.sjzs.model.http.hilt

import com.example.sjzs.model.bean.PhotoList
import com.example.sjzs.model.bean.VideoListData

/**
 * 顶层请求接口
 */
interface IHttpProcesser {

    fun getChinaBanner(url: String, callback: IHttpCallback<String>)

    fun getArticle(id: String, callback: IHttpCallback<String>)

    fun getPhoto(id: String, callback: IHttpCallback<String>)
    fun getMilitaryPhoto(id: String, callback: IHttpCallback<String>)

    fun <T> post(url: String, params: Map<String, Any>, callback: IHttpCallback<T>)

    fun getPhotoBanner(url: String, callback: IHttpCallback<PhotoList>)

    fun getVideoBanner(s:String,n:String,callback: IHttpCallback<VideoListData>)
}