package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import com.dertefter.data.dto.money.MoneyItemDto

@Entity(tableName = "money", primaryKeys = ["login", "year"])
data class MoneyEntity(
    val login: String,
    val year: String,
    val moneyItems: List<MoneyItemDto>
)
