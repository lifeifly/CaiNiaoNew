package com.example.sjzs.model.paging

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import com.example.sjzs.helper.Utils
import com.example.sjzs.model.bean.ChinaList
import com.example.sjzs.model.bean.RollData
import com.example.sjzs.model.http.PHOTO_BASE_URL
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.http.retrofit.JsonHttp
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

//enum class InitialNetworkStatus {
//    //首次加载
//    INITIAL_LOADING,
//
//    //首次加载失败
//    INITIAL_FAILED,
//
//    //首次加载成功
//    INITIAL_SUCCESS,
//
//    //首次加载没有更多数据
//    INITIAL_NONE_MORE_DATA,
//
//    //首次加载网络无连接
//    INITIAL_NO_NETWORK,
//}
//
////三种状态正在加载，加载失败，加载完成
//enum class AFTERNetwordStatus {
//
//
//    //加载中
//    LOADING,
//
//    //加载失败
//    FAILED,
//
//    //加载成功
//    SUCCESS,
//
//    //没有更多数据
//    NONE_MORE_DATA,
//
//    //网络无连接
//    NO_NETWORK
//
//
//}

class ListDataSource(private val context: Context,private val category:String) : PagingSource<Int, RollData>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RollData> {

        return try {
            val page = params.key ?: 1
            if (page==2){
               LoadResult.Error<Int,RollData>(Exception("没有更多的数据了"))
            }
            if (Utils.isNetworkConnected(context)) {
                val userHttp =
                    JsonHttp(PHOTO_BASE_URL, false)
                val data = userHttp.getService(ApiService::class.java)
                    .getPhotoList(category)
                Log.d("TAG456", "load: "+data.rollData.size)
                LoadResult.Page(data.rollData, null, page + 1)
            } else {
                LoadResult.Error(Exception("网络异常"))
            }
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}

//    var afterRetry: (() -> Any)? = null
//    var initialRetry: (() -> Any)? = null
//    private val _networkStatus = MutableLiveData<AFTERNetwordStatus>()
//    private val _initialNetworkStatus = MutableLiveData<InitialNetworkStatus>()
//    val afterNetwordStatus: LiveData<AFTERNetwordStatus> = _networkStatus
//    val initialNetworkStatus: LiveData<InitialNetworkStatus> = _initialNetworkStatus
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, ListBean>
//    ) {
//        initialRetry = null
//        if (Utils.isNetworkConnected(context)) {
//            Log.d("DataSource", "onSuccess: 1")
//            //开始加载
//            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_LOADING)
//            HttpManager.requestCommonData(
//                "china",
//                "1",
//                object : ResponseObserver<CommonData>() {
//                    override fun onSuccess(t: CommonData?) {
//                        Log.d("DataSource", "onSuccess: ")
//                        if (t == null) {
//                            //保存函数
//                            initialRetry = { loadInitial(params, callback) }
//                            //加载失败
//                            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_FAILED)
//                        } else {
//                            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_SUCCESS)
//                            callback.onResult(t.list, null, 2)
//                        }
//                    }
//
//                    override fun onFailure(e: Throwable) {
//                        Log.d("DataSource", "onFailure: ${e.message}")
//                        if (e.message == "timeout") {
//                            //没有更多数据了
//                            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_NONE_MORE_DATA)
//                        } else {
//                            //加载失败
//                            //保存函数
//                            initialRetry = { loadInitial(params, callback) }
//                            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_FAILED)
//                        }
//
//                    }
//                })
//        } else {
//            initialRetry = { loadInitial(params, callback) }
//            _initialNetworkStatus.postValue(InitialNetworkStatus.INITIAL_NO_NETWORK)
//        }
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ListBean>) {
//        afterRetry = null
//        if (Utils.isNetworkConnected(context)) {
//            //开始加载
//            _networkStatus.postValue(AFTERNetwordStatus.LOADING)
//            HttpManager.requestCommonData(
//                "china",
//                "${params.key}",
//                object : ResponseObserver<CommonData>() {
//                    override fun onSuccess(t: CommonData?) {
//                        Log.d("OkHttp", "onSuccess: ")
//                        if (t == null) {
//                            //保存函数
//                            afterRetry = { loadAfter(params, callback) }
//                            //加载失败
//                            _networkStatus.postValue(AFTERNetwordStatus.FAILED)
//                        } else {
//                            _networkStatus.postValue(AFTERNetwordStatus.SUCCESS)
//                            callback.onResult(t.list, params.key + 1)
//                        }
//                    }
//
//                    override fun onFailure(e: Throwable) {
//                        if (e.message == "timeout") {
//                            _networkStatus.postValue(AFTERNetwordStatus.NONE_MORE_DATA)
//                        } else {
//                            afterRetry = { loadAfter(params, callback) }
//                            //加载失败
//                            _networkStatus.postValue(AFTERNetwordStatus.FAILED)
//                            Log.d("OkHttp", "onFailure: ")
//                        }
//                    }
//                })
//        } else {
//            afterRetry = { loadAfter(params, callback) }
//            _networkStatus.postValue(AFTERNetwordStatus.NO_NETWORK)
//        }
//
//    }


