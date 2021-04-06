package com.example.sjzs.model

import android.util.Log
import com.example.network.api.observer.BaseObserver
import com.example.sjzs.model.bean.CommonData
import com.example.sjzs.model.http.ApiService
import com.example.sjzs.model.http.JsonHttp
import com.example.sjzs.model.observer.ResponseObserver
import io.reactivex.internal.schedulers.ExecutorScheduler
import okhttp3.ResponseBody
import java.util.concurrent.Executors
import javax.crypto.ExemptionMechanism


class DataManager {

    companion object {
        fun requestCommonData(
            category: String,
            t: String,
            responseObserver: ResponseObserver<CommonData>
        ) {
            val userHttp =
                JsonHttp("https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/", true)
            userHttp.getService(ApiService::class.java)
                .getCommonData(category, t)
                .compose(userHttp.applySchedulers(object : BaseObserver<CommonData>() {
                    override fun onSuccess(t: CommonData) {
                        responseObserver.onSuccess(t)
                    }

                    override fun onFailure(e: Throwable) {
                        responseObserver.onFailure(e)
                    }
                }))
        }

        fun requestArticleData(
            url: String,
            responseObserver: ResponseObserver<String>
        ) {
            val baseUrl = "https://news.cctv.com/"
            val userHttp = JsonHttp(baseUrl, false)
            val realId = url.replace("https://news.cctv.com/", "")
                .replace(".shtml", "")
            userHttp.getNoConvertRetrofit(ApiService::class.java).create(ApiService::class.java)
                .getArticle(realId)
                .compose(userHttp.applySchedulers(object :
                    BaseObserver<ResponseBody>() {
                    override fun onSuccess(t: ResponseBody) {
                        if (t != null) {
                            responseObserver.onSuccess(t.string())
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        responseObserver.onFailure(e)
                    }
                }))
        }

        /**
         * 请求广告
         */
        fun requestCommonBanner(
            id: String,
            responseObserver: ResponseObserver<String>
        ) {
            val baseUrl = "https://news.cctv.com/"
            val userHttp = JsonHttp("https://news.cctv.com/", false)
            userHttp.getNoConvertRetrofit(ApiService::class.java).create(ApiService::class.java)
                .getCommonBanner(id)
                .compose(userHttp.applySchedulers(object :
                    BaseObserver<ResponseBody>() {
                    override fun onSuccess(t: ResponseBody) {
                        if (t != null) {
                            responseObserver.onSuccess(t.string())
                        }
                    }

                    override fun onFailure(e: Throwable) {
                        responseObserver.onFailure(e)
                    }
                }))

        }
    }

    /**
     * 请求视频数据
     */
    fun requestVideo(
        url: String,
        id: String
    ) {
        val executorService = Executors.newFixedThreadPool(3)

        val userHttp =
            JsonHttp("https://newcntv.qcloudcdn.com/asp/hls/1200/0303000a/3/default/", false)
        var i = 1
        for (i in 1..100) {
            executorService.execute {
                userHttp.getNoConvertRetrofit(ApiService::class.java).create(ApiService::class.java)
                    .getVideo(url, i.toString())
                    .compose(userHttp.applySchedulers(object :
                        BaseObserver<ResponseBody>() {
                        override fun onSuccess(t: ResponseBody) {

                        }

                        override fun onFailure(e: Throwable) {
                            executorService.shutdown()
                        }
                    }))
            }
        }

    }
}


