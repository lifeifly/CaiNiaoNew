package com.example.sjzs.model.http

import com.example.sjzs.model.bean.CommonData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //"https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/china_2.jsonp?"
    @GET("{category}"+"_"+"{t}"+".jsonp?")
    fun getCommonData(
        @Path("category") category: String,
        @Path("t") t: String
    ): Observable<CommonData>

    @GET("{id}" + ".xml")
    fun getArticle(@Path("id") id: String): Observable<ResponseBody>

    @GET("{category}")
    fun getCommonBanner(@Path("category") category: String): Observable<ResponseBody>

    @GET("{url}"+"{id}" + ".ts")
    fun getVideo(@Path("url") url:String,@Path("id") id:String): Observable<ResponseBody>
}