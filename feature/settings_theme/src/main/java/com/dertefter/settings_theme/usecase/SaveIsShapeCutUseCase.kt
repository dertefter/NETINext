package com.dertefter.settings_theme.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveIsShapeCutUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(isCut: Boolean) {
        settingsRepository.saveIsShapeCut(isCut)
    }
}
