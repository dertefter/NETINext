package com.dertefter.neticlient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.data.repository.UserRepository
import com.dertefter.neticlient.presentation.ThemeState
import com.dertefter.neticlient.WearCommunicationClient
import com.dertefter.neticlient.widgets.near_schedule.WidgetUpdater
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    settingsRepository: SettingsRepository,
    private val groupRepository: GroupsRepository,
    private val userRepository: UserRepository,
    private val scheduleRepository: ScheduleRepository,
    private val widgetUpdater: WidgetUpdater,
    private val wearClient: WearCommunicationClient
) : ViewModel() {


    private val _themeColor = settingsRepository.themeColor
    private val _isShapeCut = settingsRepository.isShapeCut
    private val _newColorSpecVersion = settingsRepository.oldColorSpecVersion
    private val _themeStyle = settingsRepository.themeStyle

    val themeState: StateFlow<ThemeState> = combine(
        _themeColor,
        _isShapeCut,
        _themeStyle,
        _newColorSpecVersion
    ) { themeColor, isShapeCut, themeStyle, newColorSpecVersion ->
        ThemeState(themeColor, isShapeCut, themeStyle, newColorSpecVersion)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeState()
    )

    private val _isNotificationEnabled = settingsRepository.isNotificationEnabled

    val currentGroup: StateFlow<GroupDto?> = groupRepository.getCurrentGroup()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentSchedule: StateFlow<List<TimeSlotDto>?> = currentGroup.flatMapLatest { group ->
        if (group != null) {
            scheduleRepository.getSchedule(group)
        } else {
            flowOf(null)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )



    init {
        merge(authRepository.ciuAuthStatus, authRepository.yourNetiAuthStatus)
            .onEach { authStatus ->
                if (authStatus is AuthStatus.Authorized) {
                    userRepository.updateUserInfo()
                }
            }
            .launchIn(viewModelScope)

        userRepository.getUserInfo()
            .distinctUntilChanged()
            .onEach { userInfo ->
                userInfo?.symGroup?.let { symGroup ->
                    groupRepository.setCurrentGroup(
                        GroupDto(symGroup, true)
                    )
                }

            }
            .launchIn(viewModelScope)

        currentSchedule.onEach {
            widgetUpdater.updateScheduleWidget()
        }.launchIn(viewModelScope)

        currentGroup.onEach { group ->
            group?.let {
                wearClient.sendData("group", "${group.name}=${group.isIndividual}")
            }
        }.launchIn(viewModelScope)

        scheduleRepository.getWeekHeader()
            .onEach {
                widgetUpdater.updateWeekHeaderWidget()
            }.launchIn(viewModelScope)

        viewModelScope.launch {
            scheduleRepository.updateWeekHeader()
        }

        authRepository.authCreds
            .distinctUntilChanged()
            .onEach { creds ->
                creds?.let {
                    if (authRepository.ciuAuthStatus.first() != AuthStatus.Authorized(it.xLogin)){
                        authRepository.authorizeFull(it.xLogin, it.xPassword)
                    }
                    wearClient.sendData("xLogin", it.xLogin)
                    wearClient.sendData("xPassword", it.xPassword)
                }
            }
            .launchIn(viewModelScope)
    }



    val screenState: StateFlow<MainScreenState?> = combine(
        authRepository.ciuAuthStatus,
        authRepository.yourNetiAuthStatus,
        _isNotificationEnabled,
    ) { authStatusCiu, authStatusYourNeti, isNotificationEnabled ->
        MainScreenState(authStatusCiu, authStatusYourNeti, isNotificationEnabled ?: false )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnRetryAuthorizeCiu -> {
                retryAuthorize()
            }

            is Event.OnRetryAuthorizeYourNeti -> {
                retryAuthorizeYourNeti()
            }
        }
    }

    private fun retryAuthorize() {
        viewModelScope.launch {
            authRepository.authCreds.first()?.let {
                authRepository.authorizeFull(it.xLogin, it.xPassword)
            }
        }
    }

    private fun retryAuthorizeYourNeti() {
        viewModelScope.launch {
            authRepository.authCreds.first()?.let {
                authRepository.authorizeYourNeti(it.xLogin, it.xPassword)
            }
        }
    }
}
