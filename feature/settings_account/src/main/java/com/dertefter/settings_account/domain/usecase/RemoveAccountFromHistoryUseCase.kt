package com.dertefter.settings_account.domain.usecase

import com.dertefter.data.repository.AuthRepository
import com.dertefter.navigation.Navigator
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoveAccountFromHistoryUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String) {
        authRepository.removeFromAccountHistory(login)
    }
}