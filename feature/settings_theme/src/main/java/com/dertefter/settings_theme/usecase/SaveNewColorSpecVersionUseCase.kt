package com.dertefter.settings_theme.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveNewColorSpecVersionUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(newColorSpecVersion: Boolean) = settingsRepository.saveNewColorSpecVersion(newColorSpecVersion)
}
