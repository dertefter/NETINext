package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShareScoreRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : ShareScoreRepository {

    override fun getShareScoreLink(): Flow<String?> =
        localDataSource.getShareScoreLink()

    override suspend fun updateShareScoreLink(generateNew: Boolean): Result<String> {
        return remoteDataSource.updateShareScoreLink(generateNew).onSuccess {
            localDataSource.saveShareScoreLink(it)
        }
    }


}