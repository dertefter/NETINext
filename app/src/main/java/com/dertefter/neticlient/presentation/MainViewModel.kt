package com.dertefter.neticlient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val groupRepository: GroupsRepository,
    private val userRepository: UserRepository,
) : ViewModel() {


    private val _themeColor = settingsRepository.themeColor
    private val _isShapeCut = settingsRepository.isShapeCut

    private val _isNotificationEnabled = settingsRepository.isNotificationEnabled


    init {
        userRepository.getUserInfoDto()
            .distinctUntilChanged()
            .onEach { userInfo ->
                userInfo?.symGroup?.let { symGroup ->
                    groupRepository.setCurrentGroup(
                        GroupDto(symGroup, true)
                    )
                }

            }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val authStatusNotify = authRepository.ciuAuthStatus.transformLatest { status ->
        when (status) {
            is AuthStatus.Loading, is AuthStatus.Error -> {
                emit(status)
            }
            is AuthStatus.Authorized -> {
                userRepository.updateUserInfoDto()
                emit(status)
                delay(3000)
                emit(null)

            }
            is AuthStatus.Unauthorized -> {
                emit(null)
            }
        }
    }

    val screenState: StateFlow<MainScreenState?> = combine(
        authRepository.ciuAuthStatus,
        authStatusNotify,
        _themeColor,
        _isShapeCut,
        _isNotificationEnabled,
    ) { authStatus, authStatusNotify, themeColor, isShapeCut, isNotificationEnabled ->
        MainScreenState(authStatus, authStatusNotify, themeColor,isShapeCut, isNotificationEnabled ?: false )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        authRepository.authCreds
            .distinctUntilChanged()
            .onEach { creds ->
                creds?.let {
                    if (authRepository.ciuAuthStatus.first() != AuthStatus.Authorized(it.xLogin)){
                        authRepository.authorizeFull(it.xLogin, it.xPassword)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRetryAuthorize -> {
                retryAuthorize()
            }
        }
    }

    fun retryAuthorize() {
        viewModelScope.launch {
            authRepository.authCreds.first()?.let {
                authRepository.authorizeFull(it.xLogin, it.xPassword)
            }
        }
    }
}
