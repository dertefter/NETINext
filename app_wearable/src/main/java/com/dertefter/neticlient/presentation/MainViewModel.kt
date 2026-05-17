package com.dertefter.neticlient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.AuthRepository
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val groupRepository: GroupsRepository,
    private val userRepository: UserRepository,
    private val scheduleRepository: ScheduleRepository,
) : ViewModel() {


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
}
