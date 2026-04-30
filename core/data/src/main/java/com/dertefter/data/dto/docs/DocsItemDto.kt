package com.dertefter.data.dto.docs


data class DocsItemDto(
    val type: String,
    val date: String?,
    val status: String?,
    val person: String?,
    val comment: String?,
    val number: String?
)
