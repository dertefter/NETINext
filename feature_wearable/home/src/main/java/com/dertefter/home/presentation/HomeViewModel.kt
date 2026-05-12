package com.dertefter.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.navigation_wearable.Navigator
import com.dertefter.navigation_wearable.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val groupsRepository: GroupsRepository,
    private val navigator: Navigator
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

    private val ticker = flow {
        while (true) {
            emit(Unit)
            delay(3000)
        }
    }

    private val nextDayData = combine(
        currentSchedule, ticker
    ) { schedule, _ ->
        val today = LocalDate.now()
        val nowTime = LocalTime.now()
        val grouped = schedule?.groupBy { it.getDate() } ?: return@combine null

        val todayLessons = grouped[today]
        val targetDate =
            if (todayLessons != null && todayLessons.any { it.getEndTime().isAfter(nowTime) }) {
                today
            } else {
                grouped.keys.filter { it.isAfter(today) }.minOrNull()
            }

        targetDate?.let { it to grouped[it] }
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = null
    )

    val scheduleState: StateFlow<ScheduleState> = combine(
        nextDayData,
        _isScheduleLoading,
        _scheduleError,
        currentGroup
    ) { data, isLoading, error, group ->
        ScheduleState(
            date = data?.first,
            timeSlots = data?.second ?: emptyList(),
            group = group,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateSchedule -> {
                val group = currentGroup.value ?: return
                refreshSchedule(group)
            }
            Event.OnOpenDetails -> {
                navigator.navigate(Routes.Calendar)
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
    val date: LocalDate? = null,
    val group: GroupDto? = null,
    val timeSlots: List<TimeSlotDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)


