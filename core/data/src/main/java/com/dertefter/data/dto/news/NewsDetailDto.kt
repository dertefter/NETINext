package com.dertefter.data.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class NewsDetailDto(
    val title: String,
    val contentHtml: String?,
    val imageUrls: List<String>)