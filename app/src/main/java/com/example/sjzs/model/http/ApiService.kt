package com.example.sjzs.model.http

import com.example.sjzs.model.bean.CommonData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //"https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/world_1.jsonp?cb=world&n=100"
    @GET("{category}")
    fun getCommonData(
        @Path("category") category: String,
        @Query("cb") cb: String,
        @Query("n") n: String
    ): Observable<CommonData>

    @GET("{id}" + ".xml")
    fun getArticle(@Path("id") id: String): Observable<ResponseBody>

    @GET("{category}")
    fun getCommonBanner(@Path("category") category: String): Observable<ResponseBody>

    @GET("{url}"+"{id}" + ".ts")
    fun getVideo(@Path("url") url:String,@Path("id") id:String): Observable<ResponseBody>
}