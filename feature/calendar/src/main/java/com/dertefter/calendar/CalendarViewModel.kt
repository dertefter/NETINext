package com.dertefter.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.calendar.domain.usecase.*
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.UiState
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCurrentGroupUseCase: GetCurrentGroupUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getWeeksBoundsUseCase: GetWeeksBoundsUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val navigateBackUseCase: NavigateBackUseCase,
    private val openGroupSearchUseCase: OpenGroupSearchUseCase,
    private val navigateLessonDetailUseCase: NavigateLessonDetailUseCase,
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _scheduleError = MutableStateFlow<AppError?>(null)

    private val _currentGroup = getCurrentGroupUseCase()
        .distinctUntilChanged()
        .onEach { group -> group?.let { refreshSchedule(it) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> = combine(
        _currentGroup,
        _currentGroup.flatMapLatest { group ->
            if (group != null) getScheduleUseCase(group) else flowOf(null)
        },
        getWeeksBoundsUseCase(),
        _isUpdating,
        _scheduleError
    ) { args: Array<Any?> ->
        val group = args[0] as GroupDto?
        val schedule = args[1] as List<TimeSlotDto>?
        val weekBounds = args[2] as List<WeekBoundsDto>?
        val updating = args[3] as Boolean
        val error = args[4] as AppError?

        UiState(
            group = group,
            timeSlots = schedule ?: emptyList(),
            weekBounds = weekBounds ?: emptyList(),
            isUpdating = updating,
            isError = error,
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
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnOpenGroupSearch -> {
                openGroupSearchUseCase()
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
