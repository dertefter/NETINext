package com.dertefter.data.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsItem(
    val id: String,
    val type: String,
    val title: String,
    val tags: String,
    val date: String,
    val imageUrl: String?,
    val detailUrl: String,
)