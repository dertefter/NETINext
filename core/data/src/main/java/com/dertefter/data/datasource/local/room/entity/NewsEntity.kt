package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dertefter.data.dto.news.NewsItem

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val tags: String,
    val date: String,
    val imageUrl: String?,
    val detailUrl: String,
    val page: Int,
    val position: Int
)

fun NewsEntity.toDomain() = NewsItem(
    id = id,
    type = type,
    title = title,
    tags = tags,
    date = date,
    imageUrl = imageUrl,
    detailUrl = detailUrl
)

fun NewsItem.toEntity(page: Int, position: Int) = NewsEntity(
    id = id,
    type = type,
    title = title,
    tags = tags,
    date = date,
    imageUrl = imageUrl,
    detailUrl = detailUrl,
    page = page,
    position = position
)
