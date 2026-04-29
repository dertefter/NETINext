package com.dertefter.neticlient.screens.schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val groupsRepository: GroupsRepository
) : ViewModel() {

    private val _isScheduleLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _scheduleError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    val currentGroup: StateFlow<GroupDto?> = groupsRepository.getCurrentGroup()
        .distinctUntilChanged()
        .onEach { group ->
            group?.let { refreshSchedule(it) }
        }
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

    val scheduleState: StateFlow<ScheduleState> = combine(
        currentSchedule,
        _isScheduleLoading,
        _scheduleError,
        currentGroup
    ) { schedule, isLoading, error, group ->
        val grouped = schedule?.groupBy { it.getDate() } ?: emptyMap()
        val sortedDates = grouped.keys.sorted()
        ScheduleState(
            dates = sortedDates,
            scheduleByDate = grouped,
            group = group,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleState()
    )

    fun onEvent(event: ScheduleEvent) {
        when (event) {
            is ScheduleEvent.OnUpdateSchedule -> {
                val group = currentGroup.value ?: return
                refreshSchedule(group)
            }
        }
    }

    private fun refreshSchedule(group: GroupDto) {
        viewModelScope.launch {
            _isScheduleLoading.update { true }
            _scheduleError.update { null }
            scheduleRepository.updateScheduleForGroup(group).onFailure { e ->
                _scheduleError.update {
                    e.toAppError()
                }
            }
            _isScheduleLoading.update { false }
        }
    }
}

data class ScheduleState(
    val dates: List<LocalDate> = emptyList(),
    val scheduleByDate: Map<LocalDate, List<TimeSlotDto>> = emptyMap(),
    val group: GroupDto? = null,
    val isLoading: Boolean = false,
    val error: AppError? = null,
)

sealed interface ScheduleEvent {
    object OnUpdateSchedule : ScheduleEvent
}
