package com.dertefter.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.calendar.domain.usecase.GetCurrentGroupUseCase
import com.dertefter.calendar.domain.usecase.GetEventsUseCase
import com.dertefter.calendar.domain.usecase.GetScheduleUseCase
import com.dertefter.calendar.domain.usecase.GetSessiaScheduleUseCase
import com.dertefter.calendar.domain.usecase.GetWeeksBoundsUseCase
import com.dertefter.calendar.domain.usecase.NavigateBackUseCase
import com.dertefter.calendar.domain.usecase.NavigateLessonDetailUseCase
import com.dertefter.calendar.domain.usecase.OpenGroupSearchUseCase
import com.dertefter.calendar.domain.usecase.UpdateEventsUseCase
import com.dertefter.calendar.domain.usecase.UpdateScheduleUseCase
import com.dertefter.calendar.domain.usecase.UpdateSessiaScheduleUseCase
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.UiState
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCurrentGroupUseCase: GetCurrentGroupUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getSessiaScheduleUseCase: GetSessiaScheduleUseCase,
    getWeeksBoundsUseCase: GetWeeksBoundsUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val updateSessiaScheduleUseCase: UpdateSessiaScheduleUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val openGroupSearchUseCase: OpenGroupSearchUseCase,
    private val navigateLessonDetailUseCase: NavigateLessonDetailUseCase,
    getEventsUseCase: GetEventsUseCase,
    private val updateEventsUseCase: UpdateEventsUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _scheduleError = MutableStateFlow<AppError?>(null)

    private val _events = getEventsUseCase()

    private val _currentGroup = getCurrentGroupUseCase()
        .distinctUntilChanged()
        .onEach { group -> group?.let { refreshSchedule(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @Suppress("Unchecked_cast")
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> = combine(
        _currentGroup,
        _currentGroup.flatMapLatest { group ->
            if (group != null) getScheduleUseCase(group) else flowOf(null)
        },
        _currentGroup.flatMapLatest { group ->
            if (group != null) getSessiaScheduleUseCase(group) else flowOf(null)
        },
        getWeeksBoundsUseCase(),
        _isUpdating,
        _scheduleError,
        _events
    ) { args: Array<Any?> ->
        val group = args[0] as GroupDto?
        val schedule = args[1] as List<TimeSlotDto>?
        val sessiaSchedule = args[2] as List<TimeSlotDto>?
        val weekBounds = args[3] as List<WeekBoundsDto>?
        val updating = args[4] as Boolean
        val error = args[5] as AppError?
        val events = args[6] as List<EventDto>?

        UiState(
            group = group,
            timeSlots = schedule ?: emptyList(),
            sessiaTimeSlots = sessiaSchedule ?: emptyList(),
            weekBounds = weekBounds ?: emptyList(),
            isUpdating = updating,
            isError = error,
            events = events ?: emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    private fun refreshSchedule(group: GroupDto) {
        viewModelScope.launch {
            _isUpdating.value = true
            _scheduleError.value = null
            updateScheduleUseCase(group).onFailure { e ->
                _scheduleError.value = e.toAppError()
            }
            _isUpdating.value = false
            updateSessiaScheduleUseCase(group)

        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnOpenGroupSearch -> {
                openGroupSearchUseCase()
            }

            is Event.OnUpdateEvents -> {
                viewModelScope.launch {
                    updateEventsUseCase(event.year, event.month)
                }
            }

            is Event.OnNavigateBack -> {
                navigateBackUseCase()
            }

            is Event.OnUpdateSchedule -> {
                viewModelScope.launch {
                    val group = getCurrentGroupUseCase().first()
                    group?.let {
                        refreshSchedule(group)
                    }
                  
                }
            }

            is Event.OnOpenLessonDetail -> {
                navigateLessonDetailUseCase(
                    name = event.name,
                    type = event.type,
                    aud = event.aud,
                    personIds = event.personIds,
                    startTime = event.startTime,
                    endTime = event.endTime,
                    date = event.date,

                )
            }
        }
    }
}
