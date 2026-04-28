package com.dertefter.data.repository

import com.dertefter.data.dto.user.LksDto
import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfo(): Flow<UserInfoDto?>

    suspend fun updateUserInfo(): Result<UserInfoDto?>

    fun getLksList(): Flow<List<LksDto>?>

    suspend fun updateLksList(): Result<List<LksDto>?>

    suspend fun setSelectedLks(lksId: Int): Result<Unit>

}