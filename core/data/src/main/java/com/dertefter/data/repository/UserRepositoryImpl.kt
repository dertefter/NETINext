package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : UserRepository {


    override fun getUserInfoDto(): Flow<UserInfoDto?> {
        return localDataSource.getUserInfo()
    }

    override suspend fun updateUserInfoDto(): Result<UserInfoDto?> {
        return remoteDataSource.getUserInfo().onSuccess {
            localDataSource.saveUserInfo(it)
        }
    }

}