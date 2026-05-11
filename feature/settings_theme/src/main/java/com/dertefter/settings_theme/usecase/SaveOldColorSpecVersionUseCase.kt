package com.dertefter.settings_theme.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveOldColorSpecVersionUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(newColorSpecVersion: Boolean) = settingsRepository.saveOldColorSpecVersion(newColorSpecVersion)
}
