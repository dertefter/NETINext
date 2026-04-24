package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dertefter.data.dto.messsages.MessageDto

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Long,
    val userLogin: String,
    val messageDto: MessageDto
)
