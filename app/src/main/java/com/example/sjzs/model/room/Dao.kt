package com.example.sjzs.model.room

import androidx.paging.PagingSource
import androidx.room.*
import com.example.sjzs.model.bean.*

@Dao
interface ChinaListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(chinaLists: List<ChinaList>)

    @Delete
    fun deleteListBeans(vararg chinaLists: ChinaList)

    @Query("DELETE  FROM china_table")
    fun deleteAllListBeans()

    @Query("SELECT id,title,image,url FROM china_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Query("SELECT * FROM china_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, ChinaList>
}

@Dao
interface WorldListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(worldLists: List<WorldList>)

    @Query("SELECT id,title,image,url FROM world_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg worldList: WorldList)

    @Query("DELETE  FROM world_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM world_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, WorldList>
}

@Dao
interface SocietyListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(societyLists: List<SocietyList>)

    @Query("SELECT id,title,image,url FROM society_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg societyList: SocietyList)

    @Query("DELETE  FROM society_table")
    fun deleteAllListBeans()


    @Query("SELECT * FROM society_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, SocietyList>
}

@Dao
interface LawListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(lawLists: List<LawList>)

    @Query("SELECT id,title,image,url FROM law_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg lawList: LawList)

    @Query("DELETE  FROM law_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM law_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, LawList>
}

@Dao
interface EnterListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(enterLists: List<EntertainmentList>)

    @Query("SELECT id,title,image,url FROM entertainment_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg enterList: EntertainmentList)

    @Query("DELETE  FROM entertainment_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM entertainment_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, EntertainmentList>
}

@Dao
interface TechListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(techLists: List<TechList>)

    @Query("SELECT id,title,image,url FROM tech_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg techList: TechList)

    @Query("DELETE  FROM tech_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM tech_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, TechList>
}

@Dao
interface LifeListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(lifeLists: List<LifeList>)

    @Query("SELECT id,title,image,url FROM life_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg lifeList: LifeList)

    @Query("DELETE  FROM life_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM life_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, LifeList>
}

@Dao
interface VideoListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(videoLists: List<VideoList>)

    @Query("SELECT id,title,image,url FROM video_table WHERE title LIKE :keyword")
    suspend fun queryByKeyword(keyword: String): List<SearchBean>

    @Delete
    fun deleteListBeans(vararg videoLists: VideoList)

    @Query("DELETE  FROM video_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM video_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int, VideoList>
}

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(searchHistory: List<SearchHistory>)

    @Insert
    suspend fun insertSingleBean(searchHistory: SearchHistory)

    @Query("DELETE FROM history_table WHERE keyword = :text")
    suspend fun deleteListBeans(text: String)

    @Query("DELETE  FROM history_table")
    fun deleteAllListBeans()

    @Query("SELECT * FROM history_table ORDER BY date DESC")
    suspend fun queryAllListBeans(): List<SearchHistory>
}

@Dao
interface CollectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListBeans(collectBeans: List<CollectBean>)

    @Insert
    suspend fun insertSingleBean(collectBean: CollectBean)

    @Query("SELECT * FROM collect_table WHERE url = :url")
    suspend fun queryByUrl(url:String):CollectBean?

    @Query("DELETE  FROM collect_table")
    fun deleteAllListBeans()

    @Update
    fun update(vararg collectBean: CollectBean)

    @Delete
    suspend fun deleteByCollectBean(collectBean: CollectBean)

    @Query("UPDATE collect_table SET date = :time WHERE url =:url")
    fun updateByUrl(url:String,time:Long)

    @Query("DELETE  FROM collect_table WHERE url =:url")
    fun deleteSingleByUrl(url:String)

    @Query("SELECT * FROM collect_table ORDER BY date DESC")
    fun queryAllListBeans(): PagingSource<Int,CollectBean>

    @Query("SELECT * FROM collect_table ORDER BY date DESC")
    suspend fun queryAll(): List<CollectBean>
}

