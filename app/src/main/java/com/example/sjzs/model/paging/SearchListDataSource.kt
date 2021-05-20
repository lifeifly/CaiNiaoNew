package com.example.sjzs.model.paging

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.ChinaList
import com.example.sjzs.model.bean.RollData
import com.example.sjzs.model.bean.SearchBean
import com.example.sjzs.model.http.PHOTO_BASE_URL
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import com.example.sjzs.model.room.ListBeanDatabase
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception


class SearchListDataSource(private val mKeyword:String,private val data: ListBeanDatabase) :
    PagingSource<Int, SearchBean>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchBean> {

        return try {
           var page=params.key?:1

            var a=getData(page)
            while (a.size==0){
                page+=1
                a=getData(page)
            }

            Log.d("TAGADAPTER", "onBindViewHolder:1 "+a.size)
            LoadResult.Page(a, null, page + 1)
        } catch (e: IOException) {
            // IOException for network failures.
            Log.d("TAGADAPTER", "onBindViewHolder:2")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.d("TAGADAPTER", "onBindViewHolder:3 ")
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
   private suspend fun getData(i:Int):List<SearchBean>{
       return when(i){
            1->data.chinaListDao().queryByKeyword(mKeyword)
            2->data.worldListDao().queryByKeyword(mKeyword)
            3->data.societyListDao().queryByKeyword(mKeyword)
            4->data.lawListDao().queryByKeyword(mKeyword)
            5->data.enterListDao().queryByKeyword(mKeyword)
            6->data.techListDao().queryByKeyword(mKeyword)
            7->data.lifeListDao().queryByKeyword(mKeyword)
           else -> data.videoListDao().queryByKeyword(mKeyword)
       }
    }


}



