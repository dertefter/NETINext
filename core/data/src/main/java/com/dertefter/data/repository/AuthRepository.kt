package com.dertefter.data.repository

import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.auth.AuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {


    val ciuAuthStatus: StateFlow<AuthStatus>

    val yourNetiAuthStatus: StateFlow<AuthStatus>

    val authCreds: Flow<AuthCreditions?>

    suspend fun authorizeFull(
        login: String,
        password: String,
        logoutOnFail: Boolean = false,
        removeAccountOnFail: Boolean = false,
        updateStatusOnSuccessOnly: Boolean = false,
    ): Result<Unit>

    suspend fun authorizeCiu(login: String, password: String, updateStatusOnSuccessOnly: Boolean = false): Result<Unit>

    suspend fun authorizeYourNeti(login: String, password: String, updateStatusOnSuccessOnly: Boolean = false): Result<Unit>

    suspend fun saveAuthCreds(login: String, password: String)

    suspend fun switchToLogin(login: String)
    suspend fun checkAuth(): Result<Unit>

    suspend fun logout()

    suspend fun removeFromAccountHistory(login: String)

    suspend fun addToAccountHistory(login: String)

    fun getLoginHistory(): Flow<List<String>>

    fun getCurrentLogin(): Flow<String?>
}