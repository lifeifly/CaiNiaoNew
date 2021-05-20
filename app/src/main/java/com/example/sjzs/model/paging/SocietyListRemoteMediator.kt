package com.example.sjzs.model.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.sjzs.model.bean.SocietyList
import com.example.sjzs.model.bean.WorldList
import com.example.sjzs.model.http.retrofit.ApiService
import com.example.sjzs.model.room.LastUpdateTimeEntity
import com.example.sjzs.model.room.ListBeanDatabase
import com.example.sjzs.model.room.RemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SocietyListRemoteMediator(
    private val query: String,
    private val category: String,
    private val database: ListBeanDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, SocietyList>() {
    private val listBeanDao = database.societyListDao()
    private val remoteKeyDao = database.remoteKeyDao()
    private val lastUpdateTimeDao = database.lastUpdateTimeDao()

    companion object {
        //缓存时间,10分钟
        private const val CACHE_TIME = 10 * 60 * 1000
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SocietyList>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    //如果上页没有数据
                    val remoteKey: RemoteKey? = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(query)
                    }
                    var key = 0
                    if (remoteKey == null) {
                        database.withTransaction {
                            remoteKeyDao.insertOrReplace(RemoteKey(query, 1))
                        }
                        key = 1
                        key
                    } else {
                        if (remoteKey.nextKey + 1 >= 8) {
                            key = 1
                        } else {
                            key = remoteKey.nextKey + 1
                        }
                        key
                    }
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(query)
                    }

                    if (remoteKey.nextKey == 8) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    remoteKey.nextKey
                }
            }

            val response = apiService.getSocietyData(category, "${loadKey}")
            Log.d("REFRESH", "load:4 "+response)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    listBeanDao.deleteAllListBeans()
                }
                //删除旧键
                remoteKeyDao.deleteByQuery(query)
                //插入新的远程键
                remoteKeyDao.insertOrReplace(RemoteKey(query, loadKey + 1))

                listBeanDao.insertListBeans(response.list)

                //删除上次更新事件
                lastUpdateTimeDao.deleteByQuery(query)
                //添加这次的更新时间
                lastUpdateTimeDao.insertOrReplace(
                    LastUpdateTimeEntity(
                        query,
                        System.currentTimeMillis()
                    )
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = loadKey + 1 == 8
            )
        } catch (e: IOException) {

            MediatorResult.Error(e)
        } catch (e: HttpException) {

            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val lastUpdateTime =
            if (lastUpdateTimeDao.lastUpdateTimeEntityByQuery(query) == null)
                0L
            else
                lastUpdateTimeDao.lastUpdateTimeEntityByQuery(query)
                    .lastUpdateTime

        return if (System.currentTimeMillis() - lastUpdateTime >= CACHE_TIME) {
            //过期需要更新
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            //未过期
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
}