package com.example.sjzs.model.http.hilt

import com.example.sjzs.model.bean.PhotoList
import com.example.sjzs.model.bean.VideoListData

/**
 * 代理执行请求
 */
class HttpHelper private constructor() :IHttpProcesser {
    //被代理类对象
    companion object{
       private var proxyed:IHttpProcesser?=null

        fun init(iHttpProcesser: IHttpProcesser){
            this.proxyed=iHttpProcesser
        }
        //单例
      private var instances:HttpHelper?=null
        @Synchronized
        fun obtain():HttpHelper{
            if (instances==null){
                instances= HttpHelper()
            }
            return instances!!
        }
    }

    override fun  getChinaBanner(url: String, callback: IHttpCallback<String>) {
        proxyed?.getChinaBanner(url,callback)
    }

    override fun getArticle(id: String, callback: IHttpCallback<String>) {
        proxyed?.getArticle(id, callback)
    }

    override fun getPhoto(id: String, callback: IHttpCallback<String>) {

    }

    override fun getMilitaryPhoto(id: String, callback: IHttpCallback<String>) {

    }

    override fun <T> post(url: String, params: Map<String, Any>, callback: IHttpCallback<T>) {

    }

    override fun getPhotoBanner(url: String, callback: IHttpCallback<PhotoList>) {

    }

    override fun getVideoBanner(s: String,n:String, callback: IHttpCallback<VideoListData>) {

    }




}