package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dertefter.data.dto.person.PersonDetailDto

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey val personId: Long,
    val personDetailDto: PersonDetailDto
)
