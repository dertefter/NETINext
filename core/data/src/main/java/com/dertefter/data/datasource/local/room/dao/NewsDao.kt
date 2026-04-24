package com.dertefter.data.datasource.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dertefter.data.datasource.local.room.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM news ORDER BY position ASC")
    fun getNews(): PagingSource<Int, NewsEntity>

    @Query("DELETE FROM news")
    suspend fun clearAll()
}
