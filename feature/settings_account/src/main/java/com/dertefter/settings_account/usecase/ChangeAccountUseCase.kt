package com.dertefter.settings_account.usecase

import com.dertefter.data.repository.AuthRepository
import javax.inject.Inject

class ChangeAccountUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String) {
        authRepository.switchToLogin(login)
    }
}