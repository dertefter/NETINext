package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.money.MoneyItemDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoneyRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : MoneyRepository {
    override fun getMoneyForYear(year: String): Flow<List<MoneyItemDto>> {
        return localDataSource.getMoneyForYear(year).map { it ?: emptyList() }
    }

    override fun getYears(): Flow<List<String>> {
        return localDataSource.getMoneyYears().map { it ?: emptyList() }
    }

    override suspend fun updateMoneyForYear(year: String): Result<List<MoneyItemDto>> {
        return remoteDataSource.getMoneyForYear(year = year).onSuccess {
            localDataSource.saveMoneyForYear(year, it)
        }
    }

    override suspend fun updateYears(): Result<List<String>> {
        return remoteDataSource.getYears().onSuccess {
            localDataSource.saveMoneyYears(it)
        }
    }


}
