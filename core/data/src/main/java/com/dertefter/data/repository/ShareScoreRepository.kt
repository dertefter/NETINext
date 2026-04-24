package com.dertefter.data.repository

import kotlinx.coroutines.flow.Flow

interface ShareScoreRepository {

    fun getShareScoreLink(): Flow<String?>

    suspend fun updateShareScoreLink(generateNew: Boolean = false): Result<String>


}