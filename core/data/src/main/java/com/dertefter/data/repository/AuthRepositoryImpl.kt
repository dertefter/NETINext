package com.dertefter.data.repository

import android.util.Log
import com.dertefter.data.common.AppError
import com.dertefter.data.common.AppException
import com.dertefter.data.common.toAppError
import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.auth.AuthCreditions
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    @param:ApplicationScope private val externalScope: CoroutineScope
) : AuthRepository {

    private val _ciuAuthStatus = MutableStateFlow<AuthStatus>(AuthStatus.Unauthorized)

    private val _yourNetiAuthStatus = MutableStateFlow<AuthStatus>(AuthStatus.Unauthorized)

    override val ciuAuthStatus = _ciuAuthStatus.asStateFlow()

    override val yourNetiAuthStatus = _yourNetiAuthStatus.asStateFlow()

    override val authCreds = localDataSource.getAuthCreds()


    override suspend fun authorizeFull(
        login: String,
        password: String,
        logoutOnFail: Boolean,
        removeAccountOnFail: Boolean,
        updateStatusOnSuccessOnly: Boolean
    ): Result<Unit> = withContext(externalScope.coroutineContext + NonCancellable) {
        runCatching {
            setCiuAuthStatus(AuthStatus.Unauthorized)
            setYourNetiAuthStatus(AuthStatus.Unauthorized)
            authorizeCiu(login, password, updateStatusOnSuccessOnly).getOrThrow()
            addToAccountHistory(login)
            switchToLogin(login)
            saveAuthCreds(login, password)
            authorizeYourNeti(login, password, updateStatusOnSuccessOnly).getOrThrow()
        }.onFailure {
            if (removeAccountOnFail) { removeFromAccountHistory(login) }
            if (logoutOnFail) { logout() }
        }
    }

    private fun setCiuAuthStatus(authStatus: AuthStatus) {
        _ciuAuthStatus.value = authStatus
    }
    private fun setYourNetiAuthStatus(authStatus: AuthStatus) {
        _yourNetiAuthStatus.value = authStatus
    }



    override suspend fun authorizeCiu(
        login: String,
        password: String,
        updateStatusOnSuccessOnly: Boolean
    ): Result<Unit> {
        if (updateStatusOnSuccessOnly){
            return remoteDataSource.authorizeCiu(login, password).onSuccess {
                setCiuAuthStatus(AuthStatus.Authorized(login))
            }
        }else{
            setCiuAuthStatus(AuthStatus.Loading(login))
            return remoteDataSource.authorizeCiu(login, password).onSuccess {
                setCiuAuthStatus(AuthStatus.Authorized(login))
            }.onFailure { e ->
                setCiuAuthStatus(AuthStatus.Error(login, e.toAppError()))
            }
        }

    }

    override suspend fun saveAuthCreds(login: String, password: String) {
        localDataSource.saveAuthCreds(
            AuthCreditions(
                login, password
            )
        )
    }


    override suspend fun authorizeYourNeti(
        login: String,
        password: String,
        updateStatusOnSuccessOnly: Boolean
    ): Result<Unit> {
        if (updateStatusOnSuccessOnly){
            return remoteDataSource.authorizeYourNeti(login, password).onSuccess {
                setYourNetiAuthStatus(AuthStatus.Authorized(login))
            }
        }else{
            setYourNetiAuthStatus(AuthStatus.Loading(login))
            return remoteDataSource.authorizeYourNeti(login, password).onSuccess {
                setYourNetiAuthStatus(AuthStatus.Authorized(login))
            }.onFailure { e ->
                setYourNetiAuthStatus(AuthStatus.Error(login, e.toAppError()))
            }
        }

    }

    override suspend fun logout() {
        localDataSource.logout()
        remoteDataSource.logout()
        setCiuAuthStatus(AuthStatus.Unauthorized)
        setYourNetiAuthStatus(AuthStatus.Unauthorized)
    }

    override suspend fun removeFromAccountHistory(login: String) {
        return localDataSource.removeFromAccountHistory(login)
    }

    override suspend fun addToAccountHistory(login: String) {
        return localDataSource.addToAccountHistory(login)
    }

    override fun getLoginHistory(): Flow<List<String>> {
        return localDataSource.getLoginHistory()
    }

    override fun getCurrentLogin(): Flow<String?> {
        return localDataSource.getCurrentLogin()
    }


    override suspend fun checkAuth(): Result<Unit> {
        val login = localDataSource.getCurrentLogin().first()

        if (login.isNullOrEmpty()){
            return Result.failure(AppException(AppError.Network.Unauthorized))
        } else {
            val result = remoteDataSource.checkAuth()
                .onSuccess {
                    setCiuAuthStatus(AuthStatus.Authorized(login))
                }
                .onFailure {
                    setCiuAuthStatus(AuthStatus.Authorized(login))
                }
            return result
        }
    }

    override suspend fun switchToLogin(login: String) {
        localDataSource.switchToLogin(login)
        checkAuth()
    }

}
