package com.dertefter.data.dto.schedule

data class GroupDto(
    val name: String,
    val isIndividual: Boolean = false
)

fun GroupDto.asLowercase(): GroupDto {
    return copy(name = name.lowercase())
}
