package com.dertefter.settings_notifications.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SaveNotificationEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        settingsRepository.saveNotificationEnabled(isEnabled)
    }
}
