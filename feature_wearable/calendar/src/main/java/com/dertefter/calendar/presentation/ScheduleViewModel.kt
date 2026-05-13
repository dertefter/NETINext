package com.dertefter.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    groupsRepository: GroupsRepository
) : ViewModel() {

    private val currentGroup = groupsRepository.getCurrentGroup()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentSchedule = currentGroup.flatMapLatest { group ->
        if (group != null) {
            scheduleRepository.getSchedule(group)
        } else {
            flowOf(null)
        }
    }

    val uiState: StateFlow<ScheduleState> = currentSchedule.map { schedule ->
        val timeSlotsMap = schedule?.groupBy { it.getDate() } ?: emptyMap()
        ScheduleState(
            timeSlots = timeSlotsMap,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleState(isLoading = true)
    )
}

data class ScheduleState(
    val timeSlots: Map<LocalDate, List<TimeSlotDto>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: AppError? = null,
)

sealed interface ScheduleEvent
