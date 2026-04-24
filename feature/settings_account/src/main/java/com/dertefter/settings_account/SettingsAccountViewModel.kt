package com.dertefter.settings_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.UserRepository
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import com.dertefter.settings_account.domain.usecase.ChangeAccountUseCase
import com.dertefter.settings_account.domain.usecase.LogOutUseCase
import com.dertefter.settings_account.domain.usecase.NavigateBackUseCase
import com.dertefter.settings_account.domain.usecase.NavigateToRouteUseCase
import com.dertefter.settings_account.domain.usecase.RemoveAccountFromHistoryUseCase
import com.dertefter.settings_account.domain.usecase.RetryCiuUseCase
import com.dertefter.settings_account.domain.usecase.RetryYourNetiUseCase
import com.dertefter.settings_account.presentation.Event
import com.dertefter.settings_account.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val navigateToRouteUseCase: NavigateToRouteUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val retryYourNetiUseCase: RetryYourNetiUseCase,
    private val retryCiuUseCase: RetryCiuUseCase,
    private val changeAccountUseCase: ChangeAccountUseCase,
    private val removeAccountFromHistoryUseCase: RemoveAccountFromHistoryUseCase,

    ) : ViewModel() {


    private val _currentLogin: Flow<String?> = authRepository.getCurrentLogin()

    private val _loginHistory: Flow<List<String>> = authRepository.getLoginHistory()

    private val _ciuAuthStatus: StateFlow<AuthStatus> = authRepository.ciuAuthStatus

    private val _yourNetiAuthStatus: StateFlow<AuthStatus> = authRepository.yourNetiAuthStatus


    val state: StateFlow<UiState> = combine(
        _currentLogin, _ciuAuthStatus, _yourNetiAuthStatus, _loginHistory
    ) { login, ciuAuthStatus, yourNetiAuthStatus, loginHistory ->
        UiState(
            login = login,
            ciuAuthStatus = ciuAuthStatus,
            yourNetiAuthStatus = yourNetiAuthStatus,
            loginHistory = loginHistory.filter { it != login }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(
            login = null,
            ciuAuthStatus = AuthStatus.Unauthorized,
            yourNetiAuthStatus = AuthStatus.Unauthorized,
            loginHistory = emptyList()
        )
    )


    fun onEvent(event: Event) {
        when (event) {

            is Event.OnNavigateBack -> { navigateBackUseCase() }

            is Event.OnNavigateToAuth -> {navigateToRouteUseCase(Routes.Auth)}

            is Event.OnLogOut -> {
                viewModelScope.launch {
                    logOutUseCase()
                }
            }
            is Event.OnRetryCiu -> {
                viewModelScope.launch {
                    retryCiuUseCase()
                }
            }
            is Event.OnRetryYourNeti -> {
                viewModelScope.launch {
                    retryYourNetiUseCase()
                }
            }

            is Event.OnChangeAccount -> {
                viewModelScope.launch {
                    changeAccountUseCase(event.login)
                }
            }

            is Event.OnRemoveAccountFromHistory -> {
                viewModelScope.launch {
                    removeAccountFromHistoryUseCase(event.login)
                }
            }

        }
    }


}
