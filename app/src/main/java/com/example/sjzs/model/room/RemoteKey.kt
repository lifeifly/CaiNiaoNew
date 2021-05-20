package com.example.sjzs.model.room

import androidx.room.*

/**
 * 远程键
 */
@Entity(tableName = "remote_keys")
data class RemoteKey(@PrimaryKey val label: String, val nextKey: Int)

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE label = :query")
    suspend fun deleteByQuery(query: String)
}

@Entity(tableName = "last_update")
data class LastUpdateTimeEntity(@PrimaryKey val label: String, val lastUpdateTime: Long)
@Dao
interface LastUpdateTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(lastUpdateTime: LastUpdateTimeEntity)

    @Query("SELECT * FROM last_update WHERE label = :query")
    suspend fun lastUpdateTimeEntityByQuery(query: String): LastUpdateTimeEntity

    @Query("DELETE FROM last_update WHERE label = :query")
    suspend fun deleteByQuery(query: String)
}