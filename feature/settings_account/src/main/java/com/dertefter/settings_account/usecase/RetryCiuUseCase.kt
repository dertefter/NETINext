package com.dertefter.settings_account.usecase

import com.dertefter.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RetryCiuUseCase @Inject constructor(

    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val authCreds = authRepository.authCreds.first() ?: return Result.failure(Exception())
        return authRepository.authorizeCiu(authCreds.xLogin, authCreds.xPassword)
    }
}