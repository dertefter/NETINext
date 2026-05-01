package com.dertefter.auth.domain.usecase

import com.dertefter.data.repository.AuthRepository
import javax.inject.Inject

class AuthorizeFullUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        login: String,
        password: String,
        logoutOnFail: Boolean = true,
        removeAccountOnFail: Boolean = true,
        updateStatusOnSuccessOnly: Boolean = true
    ): Result<Unit> {
        return authRepository.authorizeFull(
            login,
            password,
            logoutOnFail,
            removeAccountOnFail,
            updateStatusOnSuccessOnly
        )
    }
}
