package com.dertefter.data.repository

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val selectedRemoteDataSource: Flow<PreferredRemoteSource>

    suspend fun setSelectedRemoteDataSource(p: PreferredRemoteSource)


    val themeColor: Flow<Long?>

    suspend fun saveThemeColor(color: Long?)


    val isShapeCut: Flow<Boolean?>

    suspend fun saveIsShapeCut(isCut: Boolean)

    val isNotificationEnabled: Flow<Boolean?>

    suspend fun saveNotificationEnabled(isEnabled: Boolean)

}