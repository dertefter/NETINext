package com.dertefter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.navigation.Routes
import com.dertefter.profile.usecase.GetCiuAuthStatusUseCase
import com.dertefter.profile.usecase.GetLksListUseCase
import com.dertefter.profile.usecase.GetUserInfoUseCase
import com.dertefter.profile.usecase.GetYourNetiAuthStatusUseCase
import com.dertefter.profile.usecase.NavigateToRouteUseCase
import com.dertefter.profile.usecase.NavigateUpUseCase
import com.dertefter.profile.usecase.UpdateLksListUseCase
import com.dertefter.profile.usecase.UpdateUserInfoUseCase
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
    getUserInfoUseCase: GetUserInfoUseCase,
    getLksListUseCase: GetLksListUseCase,
    getCiuAuthStatusUseCase: GetCiuAuthStatusUseCase,
    getYourNetiAuthStatusUseCase: GetYourNetiAuthStatusUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val updateLksListUseCase: UpdateLksListUseCase,
    private val navigateToRouteUseCase: NavigateToRouteUseCase,
    private val navigateUpUseCase: NavigateUpUseCase
) : ViewModel() {

    private val _userInfoDto = getUserInfoUseCase()

    private val _lksList = getLksListUseCase()
    private val _ciuAuthStatus = getCiuAuthStatusUseCase()

    private val _yourNetiAuthStatus = getYourNetiAuthStatusUseCase()
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
        val currentRoutesMenu = if (authStatus is AuthStatus.Unauthorized) {
            listOf(Routes.SearchPerson)
        } else {
            routesMenu
        }
        return@combine UiState(authStatus, userInfoState, currentRoutesMenu, lksList ?: emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(
            AuthStatus.Unauthorized,
            UserInfoState(),
            listOf(Routes.SearchPerson),
            lksList = emptyList()
        )
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRequestUpdate -> {
                updateLksList()
                updateUserInfo()
            }

            is Event.OnNavigateToRoute -> {
                navigateToRouteUseCase(event.route)
            }

            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }
        }
    }

    private fun updateUserInfo(){
        viewModelScope.launch {

            _userInfoState.update { it.copy(isLoading = true, error = null) }

            updateUserInfoUseCase().onFailure { e ->
                _userInfoState.update { it.copy(error = e.toAppError()) }
            }

            _userInfoState.update { it.copy(isLoading = false) }

        }
    }

    private fun updateLksList(){
        viewModelScope.launch {
            updateLksListUseCase()
        }
    }


}
