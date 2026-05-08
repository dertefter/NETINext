package com.dertefter.home.usecase

import com.dertefter.data.repository.SettingsRepository
import javax.inject.Inject

class SetIsTgShowUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(isTgShow: Boolean) {
        return settingsRepository.saveIsTgLinkShow(isTgShow)
    }
}
