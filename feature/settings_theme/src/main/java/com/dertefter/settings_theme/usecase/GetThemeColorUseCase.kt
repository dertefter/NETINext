package com.dertefter.settings_theme.usecase

import com.dertefter.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeColorUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Long?> = settingsRepository.themeColor
}
