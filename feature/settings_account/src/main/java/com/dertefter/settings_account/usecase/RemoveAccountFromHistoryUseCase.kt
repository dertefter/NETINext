package com.dertefter.settings_account.usecase

import com.dertefter.data.repository.AuthRepository
import javax.inject.Inject

class RemoveAccountFromHistoryUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String) {
        authRepository.removeFromAccountHistory(login)
    }
}