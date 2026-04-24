package com.dertefter.data.repository

import com.dertefter.data.dto.user.UserInfoDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserInfoDto(): Flow<UserInfoDto?>

    suspend fun updateUserInfoDto(): Result<UserInfoDto?>

}