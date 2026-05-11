package com.dertefter.settings_theme.usecase

import com.dertefter.data.dto.settings.ThemeStyle
import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveThemeStyleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(themeStyle: ThemeStyle) = settingsRepository.saveThemeStyle(themeStyle)
}
