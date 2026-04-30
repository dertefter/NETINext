package com.dertefter.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.UserRepository
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import com.dertefter.profile.presentation.Event
import com.dertefter.profile.presentation.UiState
import com.dertefter.profile.presentation.UserInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _userInfoDto = userRepository.getUserInfo()

    private val _lksList = userRepository.getLksList()
    private val _ciuAuthStatus = authRepository.ciuAuthStatus

    private val _yourNetiAuthStatus = authRepository.yourNetiAuthStatus
    private val _userInfoState = MutableStateFlow(UserInfoState())

    val routesMenu = listOf(Routes.SessiaResults, Routes.SearchPerson, Routes.Docs, Routes.Money)

    init {
        viewModelScope.launch {
            _userInfoDto.collect { userInfoDto ->
                _userInfoState.update {
                    it.copy(userInfo = userInfoDto)
                }
            }
        }

        viewModelScope.launch {
            _ciuAuthStatus.collect { authStatus ->
                if (authStatus is AuthStatus.Authorized){
                    updateUserInfo()
                    updateLksList()
                }
            }
        }

        viewModelScope.launch {
            _yourNetiAuthStatus.collect { authStatus ->
                if (authStatus is AuthStatus.Authorized){
                    updateUserInfo()
                    updateLksList()
                }
            }
        }
    }

    val state: StateFlow<UiState> = combine(
        _ciuAuthStatus,
        _userInfoState,
        _lksList,
    ) { authStatus, userInfoState, lksList ->
        return@combine UiState(authStatus, userInfoState, routesMenu, lksList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(AuthStatus.Unauthorized, UserInfoState(), lksList = emptyList())
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRequestUpdate -> {
                updateLksList()
                updateUserInfo()
            }

            is Event.OnNavigateToRoute -> {
                if (event.route == Routes.SwapLks) {
                    navigator.openAsBottomSheet(event.route)
                } else {
                    navigator.navigate(event.route)
                }

            }

            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }
        }
    }

    private fun updateUserInfo(){
        viewModelScope.launch {

            _userInfoState.update { it.copy(isLoading = true, error = null) }

            userRepository.updateUserInfo().onFailure { e ->
                _userInfoState.update { it.copy(error = e.toAppError()) }
            }

            _userInfoState.update { it.copy(isLoading = false) }

        }
    }

    private fun updateLksList(){
        viewModelScope.launch {
            userRepository.updateLksList()
        }
    }


}
