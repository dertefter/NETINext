package com.dertefter.settings_labs.usecase

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SetSelectedRemoteDataSourceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(p: PreferredRemoteSource) {
        settingsRepository.setSelectedRemoteDataSource(p)
    }
}
