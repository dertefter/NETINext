package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_remote_keys")
data class NewsRemoteKey(
    @PrimaryKey val newsId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
