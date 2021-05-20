package com.example.sjzs.model.room

import android.content.Context
import androidx.room.*
import com.example.sjzs.model.bean.*

@Database(entities = [CollectBean::class,SearchHistory::class,VideoList::class,LifeList::class,TechList::class,SocietyList::class,LawList::class,EntertainmentList::class,WorldList::class,ChinaList::class,RemoteKey::class,LastUpdateTimeEntity::class],version = 1,exportSchema = false)
abstract class ListBeanDatabase:RoomDatabase() {
    //国内列表访问对象
    abstract fun chinaListDao():ChinaListDao
    //国际列表访问对象
    abstract fun worldListDao():WorldListDao
    abstract fun remoteKeyDao():RemoteKeyDao
    abstract fun lastUpdateTimeDao():LastUpdateTimeDao
    abstract fun societyListDao():SocietyListDao
    abstract fun lawListDao():LawListDao
    abstract fun enterListDao():EnterListDao
    abstract fun techListDao():TechListDao
    abstract fun lifeListDao():LifeListDao
    abstract fun videoListDao():VideoListDao
    abstract fun historyDao():HistoryDao
    abstract fun collectDao():CollectDao


    companion object{
        private var INSTANCE:ListBeanDatabase?=null

        @Synchronized
        fun getDatabase(context: Context):ListBeanDatabase{
            if (INSTANCE==null){
                INSTANCE=Room.databaseBuilder(context,ListBeanDatabase::class.java,"data_db")
                    .build()
            }
            return INSTANCE!!
        }
    }
}