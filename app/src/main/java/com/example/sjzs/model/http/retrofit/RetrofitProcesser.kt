package com.example.sjzs.model.http.retrofit

import android.util.Log
import com.example.network.api.observer.BaseObserver
import com.example.sjzs.model.bean.PhotoList
import com.example.sjzs.model.bean.VideoList
import com.example.sjzs.model.bean.VideoListData
import com.example.sjzs.model.http.*
import com.example.sjzs.model.http.hilt.IHttpCallback
import com.example.sjzs.model.http.hilt.IHttpProcesser
import okhttp3.ResponseBody
import javax.inject.Inject

class RetrofitProcesser @Inject constructor():IHttpProcesser {

    override fun getChinaBanner(category: String, callback: IHttpCallback<String>) {
        val userHttp =
            JsonHttp(CONTENT_BASE_URL, false)
        userHttp.getNoConvertRetrofit(ApiService::class.java).create(
            ApiService::class.java)
            .getCommonBanner(category)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<ResponseBody>() {
                override fun onSuccess(t: ResponseBody) {
                    if (t != null) {
                        callback.onSuccess(t.string() )
                    }
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))
    }

    override fun getArticle(id: String, callback: IHttpCallback<String>) {
        val userHttp =
            JsonHttp(CONTENT_BASE_URL, false)
        val realId = id.replace("https://news.cctv.com/", "")
            .replace(".shtml", "")

        userHttp.getNoConvertRetrofit(ApiService::class.java).create(
            ApiService::class.java)
            .getArticle(realId)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<ResponseBody>() {
                override fun onSuccess(t: ResponseBody) {
                    callback.onSuccess(t.string())
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))
    }

    override fun getPhoto(id: String, callback: IHttpCallback<String>) {
        val userHttp =
            JsonHttp(PHOTO_CONTENT_BASE_URL, false)
        val realId = id.replace(PHOTO_CONTENT_BASE_URL, "")
            .replace(".shtml", "")

        userHttp.getNoConvertRetrofit(ApiService::class.java).create(
            ApiService::class.java)
            .getArticle(realId)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<ResponseBody>() {
                override fun onSuccess(t: ResponseBody) {
                    callback.onSuccess(t.string())
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))
    }

    override fun getMilitaryPhoto(id: String, callback: IHttpCallback<String>) {
        val userHttp =
            JsonHttp(MILITARY_PHOTO_BASE_URL, false)
        val realId = id.replace(MILITARY_PHOTO_BASE_URL, "")
            .replace(".shtml", "")

        userHttp.getNoConvertRetrofit(ApiService::class.java).create(
            ApiService::class.java)
            .getArticle(realId)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<ResponseBody>() {
                override fun onSuccess(t: ResponseBody) {
                    callback.onSuccess(t.string())
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))
    }


    override fun <T> post(url: String, params: Map<String, Any>, callback: IHttpCallback<T>) {

    }

    override fun getPhotoBanner(url: String, callback: IHttpCallback<PhotoList>) {
        val userHttp =
            JsonHttp(PHOTO_BASE_URL, false)
        val realId = url.replace("https://news.cctv.com/", "")
            .replace(".shtml", "")

        userHttp.getRetrofit(ApiService::class.java).create(
            ApiService::class.java)
            .getPhotoBanner(realId)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<PhotoList>() {
                override fun onSuccess(t: PhotoList) {
                    callback.onSuccess(t)
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))
    }

    override fun getVideoBanner(s: String,n:String, callback: IHttpCallback<VideoListData>) {
        val userHttp=JsonHttp(VIDEO_BASE_URL,true)

        userHttp.getRetrofit(ApiService::class.java).create(ApiService::class.java)
            .getVideoBanner(s,n)
            .compose(userHttp.applySchedulers(object :
                BaseObserver<VideoListData>() {
                override fun onSuccess(t: VideoListData) {
                    callback.onSuccess(t)
                }

                override fun onFailure(e: Throwable) {
                    callback.onError(e)
                }
            }))

    }
}