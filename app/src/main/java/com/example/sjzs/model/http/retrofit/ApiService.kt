package com.example.sjzs.model.http.retrofit

import com.example.sjzs.model.bean.*
import com.example.sjzs.model.http.videoPath
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //"https://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/china_2.jsonp?"
    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getChinaData(
        @Path("category") category: String,
        @Path("t") t: String
    ): ChinaData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getWorldData(
        @Path("category") category: String,
        @Path("t") t: String
    ): WorldData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getSocietyData(
        @Path("category") category: String,
        @Path("t") t: String
    ): SocietyData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getLawData(
        @Path("category") category: String,
        @Path("t") t: String
    ): LawData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getEntertainmentData(
        @Path("category") category: String,
        @Path("t") t: String
    ): EntertainmentData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getTechData(
        @Path("category") category: String,
        @Path("t") t: String
    ): TechData

    @GET("{category}" + "_" + "{t}" + ".jsonp?")
    suspend fun getLifeData(
        @Path("category") category: String,
        @Path("t") t: String
    ): LifeData

    @GET("{id}" + ".xml")
    fun getArticle(@Path("id") id: String): Observable<ResponseBody>

    @GET("{category}")
    fun getCommonBanner(@Path("category") category: String): Observable<ResponseBody>

    @GET("{category}")
    suspend fun getPhotoList(@Path("category") catogory: String): PhotoList

    @GET(videoPath)
    suspend fun getVideoList(@Query("s") s: String,@Query("n") n:String): VideoListData

    @GET(videoPath)
     fun getVideoBanner(@Query("s") s: String,@Query("n") n:String): Observable<VideoListData>

    @GET("{category}")
    fun getPhotoBanner(@Path("category") catogory: String): Observable<PhotoList>
}