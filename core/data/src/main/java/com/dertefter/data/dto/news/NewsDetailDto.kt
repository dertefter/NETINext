package com.dertefter.data.dto.news

data class NewsDetailDto(
    val title: String,
    val contentHtml: String?,
    val imageUrls: List<String>)