package com.dertefter.settings_theme.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveThemeColorUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(color: Long?) {
        settingsRepository.saveThemeColor(color)
    }
}
