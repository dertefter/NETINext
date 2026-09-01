package com.dertefter.data.dto.docs

import kotlinx.serialization.Serializable

@Serializable
data class DocumentOptionItem(
    val text: String,
    val value: String
)