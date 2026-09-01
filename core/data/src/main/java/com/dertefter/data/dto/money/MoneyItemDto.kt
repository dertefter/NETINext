package com.dertefter.data.dto.money

import kotlinx.serialization.Serializable

@Serializable
data class MoneyItemDto (
    val title: String,
    val text: String,
)