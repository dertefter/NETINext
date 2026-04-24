package com.dertefter.settings_account.domain.usecase

import com.dertefter.data.repository.AuthRepository
import com.dertefter.navigation.Navigator
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RetryYourNetiUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val authCreds = authRepository.authCreds.first() ?: return Result.failure(Exception("No auth creds"))
        return authRepository.authorizeYourNeti(authCreds.xLogin, authCreds.xPassword)
    }
}