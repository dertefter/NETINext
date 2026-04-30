package com.dertefter.settings_account.usecase

import com.dertefter.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoginHistoryUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return authRepository.getLoginHistory()
    }
}
