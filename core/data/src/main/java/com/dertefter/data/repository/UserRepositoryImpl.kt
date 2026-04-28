package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : UserRepository {


    override fun getUserInfo(): Flow<UserInfoDto?> {
        return localDataSource.getUserInfo()
    }

    override suspend fun updateUserInfo(): Result<UserInfoDto?> {
        return remoteDataSource.getUserInfo().onSuccess {
            localDataSource.saveUserInfo(it)
        }
    }

    override fun getLksList(): Flow<List<LksDto>?> {
        return localDataSource.getLksList()
    }

    override suspend fun updateLksList(): Result<List<LksDto>?> {
        return remoteDataSource.getLksList().onSuccess {
            localDataSource.saveLksList(it)
        }
    }

    override suspend fun setSelectedLks(lksId: Int): Result<Unit> {
        return remoteDataSource.setSelectedLks(lksId)
    }

}