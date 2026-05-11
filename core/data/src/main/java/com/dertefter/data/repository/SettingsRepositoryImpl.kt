package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.settings.ThemeStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : SettingsRepository {

    init {
        localDataSource.getSelectedRemoteDataSource().onEach {
            remoteDataSource.preferredRemoteSource = it
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    override val selectedRemoteDataSource: Flow<PreferredRemoteSource> = localDataSource.getSelectedRemoteDataSource()

    override suspend fun setSelectedRemoteDataSource(p: PreferredRemoteSource) {
        localDataSource.setSelectedRemoteDataSource(p)
    }

    override val themeColor: Flow<Long?> = localDataSource.getThemeColor()

    override suspend fun saveThemeColor(color: Long?) {
        localDataSource.saveThemeColor(color)
    }

    override val newColorSpecVersion: Flow<Boolean?> = localDataSource.getNewColorSpecVersion()

    override suspend fun saveNewColorSpecVersion(newColorSpecVersion: Boolean) {
        localDataSource.saveNewColorSpecVersion(newColorSpecVersion)
    }

    override val themeStyle: Flow<ThemeStyle?> = localDataSource.getThemeStyle()

    override suspend fun saveThemeStyle(themeStyle: ThemeStyle) {
        localDataSource.saveThemeStyle(themeStyle)
    }

    override val isShapeCut: Flow<Boolean?> = localDataSource.getIsShapeCut()

    override suspend fun saveIsShapeCut(isCut: Boolean) {
        localDataSource.saveIsShapeCut(isCut)
    }

    override val isNotificationEnabled: Flow<Boolean?> = localDataSource.getIsNotificationEnabled()

    override suspend fun saveNotificationEnabled(isEnabled: Boolean) {
        localDataSource.saveIsNotificationEnabled(isEnabled)
    }

    override val isMessagesAlertSkipped: Flow<Boolean?> = localDataSource.getIsMessagesAlertSkipped()

    override suspend fun saveIsMessagesAlertSkipped(isMessagesAlertSkipped: Boolean) {
        localDataSource.saveIsMessagesAlertSkipped(isMessagesAlertSkipped)
    }

    override val isTgLinkShow: Flow<Boolean?> = localDataSource.getIsTgLinkShow()

    override suspend fun saveIsTgLinkShow(isTgLinkShow: Boolean) {
        localDataSource.saveIsTgLinkShow(isTgLinkShow)
    }

}
