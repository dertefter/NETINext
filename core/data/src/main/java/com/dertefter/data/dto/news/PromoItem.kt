package com.dertefter.data.dto.news

import java.io.Serializable

data class PromoItem(
    val title: String,
    val imageUrl: String?,
    val link: String?,
    val subtitle: String?,
    val desc: String?
) : Serializable
