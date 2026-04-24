package com.dertefter.data.repository

import com.dertefter.data.dto.money.MoneyItemDto
import kotlinx.coroutines.flow.Flow

interface MoneyRepository {

    fun getMoneyForYear(year: String): Flow<List<MoneyItemDto>>

    fun getYears(): Flow<List<String>>

    suspend fun updateMoneyForYear(year: String): Result<List<MoneyItemDto>>

    suspend fun updateYears(): Result<List<String>>

}