package com.dertefter.data.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class PromoItem(
    val title: String,
    val imageUrl: String?,
    val link: String?,
    val subtitle: String?,
    val desc: String?
)
