package com.dertefter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.home.usecase.GetCachedPromoUseCase
import com.dertefter.home.usecase.GetCurrentGroupUseCase
import com.dertefter.home.usecase.GetNewsUseCase
import com.dertefter.home.usecase.GetScheduleUseCase
import com.dertefter.home.usecase.NavigateToCalendarUseCase
import com.dertefter.home.usecase.NavigateToNewsDetailUseCase
import com.dertefter.home.usecase.OpenLessonDetailUseCase
import com.dertefter.home.usecase.OpenSearchGroupUseCase
import com.dertefter.home.usecase.UpdatePromoUseCase
import com.dertefter.home.usecase.UpdateScheduleUseCase
import com.dertefter.home.presentation.Event
import com.dertefter.home.presentation.NewsState
import com.dertefter.home.presentation.ScheduleState
import com.dertefter.home.usecase.GetIsTgShowUseCase
import com.dertefter.home.usecase.SetIsTgShowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getNewsUseCase: GetNewsUseCase,
    getCachedPromoUseCase: GetCachedPromoUseCase,
    getCurrentGroupUseCase: GetCurrentGroupUseCase,
    getIsTgShowUseCase: GetIsTgShowUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val updatePromoUseCase: UpdatePromoUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val navigateToCalendarUseCase: NavigateToCalendarUseCase,
    private val openSearchGroupUseCase: OpenSearchGroupUseCase,
    private val navigateToNewsDetailUseCase: NavigateToNewsDetailUseCase,
    private val openLessonDetailUseCase: OpenLessonDetailUseCase,
    private val setIsTgShowUseCase: SetIsTgShowUseCase
) : ViewModel() {

    private val _newsState = MutableStateFlow(
        NewsState(
            newsPagingData = getNewsUseCase().cachedIn(viewModelScope)
        )
    )
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    private val _isScheduleLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _scheduleError: MutableStateFlow<AppError?> = MutableStateFlow(null)

    private val _isTgShow = getIsTgShowUseCase()


    val promo = getCachedPromoUseCase().map { it ?: emptyList() }


    val currentGroup: StateFlow<GroupDto?> = getCurrentGroupUseCase()
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
            getScheduleUseCase(group)
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
        currentGroup,
        _isTgShow
    ) { data, isLoading, error, group, isTgShow ->
        ScheduleState(
            date = data?.first,
            timeSlots = data?.second ?: emptyList(),
            group = group,
            isLoading = isLoading,
            error = error,
            isTgShow = isTgShow
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScheduleState()
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.RequestLoadingPromo -> {
                viewModelScope.launch {
                    updatePromoUseCase()
                }

            }

            is Event.OnNavigateToCalendar -> {
                navigateToCalendarUseCase()
            }

            is Event.OnNavigateToSearchGroup -> {
                openSearchGroupUseCase()
            }

            is Event.RequestLoadingNews -> {}

            is Event.OnHideTg -> {
                viewModelScope.launch {
                    setIsTgShowUseCase(false)
                }
            }

            is Event.OnNewsClick -> {
                navigateToNewsDetailUseCase(
                    newsId = event.newsId,
                    previewUrl = event.previewUrl,
                    type = event.type,
                    tags = event.tags,
                    date = event.date,
                    contentColor = event.contentColor,
                    link = event.link
                )
            }

            is Event.OnUpdateSchedule -> {
                val group = currentGroup.value ?: return
                refreshSchedule(group)
            }

            is Event.OnOpenLessonDetail -> {
                openLessonDetailUseCase(
                    name = event.name,
                    type = event.type,
                    aud = event.aud,
                    personIds = event.personIds,
                    startTimeString = event.startTime.toString(),
                    endTimeString = event.endTime.toString(),
                    dateString = event.date.toString()
                )
            }


        }
    }

    private fun refreshSchedule(group: GroupDto) {
        viewModelScope.launch {
            _isScheduleLoading.update { true }
            _scheduleError.update { null }
            updateScheduleUseCase(group).onFailure { e ->
                _scheduleError.update {
                    e.toAppError()
                }
            }
            _isScheduleLoading.update { false }
        }
    }

}
