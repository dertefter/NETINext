package com.dertefter.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dertefter.data.datasource.local.room.entity.NewsRemoteKey

@Dao
interface NewsRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<NewsRemoteKey>)

    @Query("SELECT * FROM news_remote_keys WHERE newsId = :newsId")
    suspend fun remoteKeysNewsId(newsId: String): NewsRemoteKey?

    @Query("DELETE FROM news_remote_keys")
    suspend fun clearRemoteKeys()
}
