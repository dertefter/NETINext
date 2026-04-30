package com.dertefter.settings_labs.usecase

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedRemoteDataSourceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<PreferredRemoteSource> {
        return settingsRepository.selectedRemoteDataSource
    }
}
