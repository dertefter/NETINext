package com.dertefter.data.repository

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.dto.settings.ThemeStyle
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val selectedRemoteDataSource: Flow<PreferredRemoteSource>

    suspend fun setSelectedRemoteDataSource(p: PreferredRemoteSource)


    val themeColor: Flow<Long?>

    suspend fun saveThemeColor(color: Long?)

    val oldColorSpecVersion: Flow<Boolean?>

    suspend fun saveOldColorSpecVersion(oldColorSpecVersion: Boolean)

    val themeStyle: Flow<ThemeStyle?>

    suspend fun saveThemeStyle(themeStyle: ThemeStyle)

    val isShapeCut: Flow<Boolean?>

    suspend fun saveIsShapeCut(isCut: Boolean)

    val isNotificationEnabled: Flow<Boolean?>

    suspend fun saveNotificationEnabled(isEnabled: Boolean)


    val isMessagesAlertSkipped: Flow<Boolean?>

    suspend fun saveIsMessagesAlertSkipped(isMessagesAlertSkipped: Boolean)

    val isTgLinkShow: Flow<Boolean?>

    suspend fun saveIsTgLinkShow(isTgLinkShow: Boolean)


}