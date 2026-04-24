package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

private fun LocalDate.atStartOfWeek(): LocalDate =
    minusDays((dayOfWeek.value - DayOfWeek.MONDAY.value + 7).toLong() % 7)

private const val INITIAL_PAGE = 600
private const val PAGE_COUNT = 1200

data class CalendarState(
    val selectedDate: LocalDate? = null,
    val isExpanded: Boolean = false,
    val viewedMonth: YearMonth? = null
)

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState,
    onStateChange: (CalendarState) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val today = remember { LocalDate.now() }
    val initialMonth = remember { YearMonth.from(today) }
    val initialWeek = remember { today.atStartOfWeek() }

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = remember {
            if (state.isExpanded) {
                val month = state.selectedDate?.let { YearMonth.from(it) } ?: initialMonth
                INITIAL_PAGE + ChronoUnit.MONTHS.between(initialMonth, month).toInt()
            } else {
                val date = state.selectedDate ?: today
                INITIAL_PAGE + ChronoUnit.WEEKS.between(initialWeek, date.atStartOfWeek()).toInt()
            }
        }
    ) { PAGE_COUNT }
    LaunchedEffect(state.isExpanded, state.selectedDate) {
        val selectedDate = state.selectedDate ?: return@LaunchedEffect
        val targetPage = if (state.isExpanded) {
            val month = YearMonth.from(selectedDate)
            INITIAL_PAGE + ChronoUnit.MONTHS.between(initialMonth, month).toInt()
        } else {
            INITIAL_PAGE + ChronoUnit.WEEKS.between(initialWeek, selectedDate.atStartOfWeek()).toInt()
        }
        if (pagerState.currentPage != targetPage) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(targetPage)
            }

        }
    }

    LaunchedEffect(pagerState.currentPage, state.isExpanded, state.selectedDate) {
        val diff = (pagerState.currentPage - INITIAL_PAGE).toLong()
        val currentViewedMonth = if (state.isExpanded) {
            initialMonth.plusMonths(diff)
        } else {
            val weekStart = initialWeek.plusWeeks(diff)
            val weekEnd = weekStart.plusDays(6)
            val isSelectedInWeek = state.selectedDate?.let { date ->
                !date.isBefore(weekStart) && !date.isAfter(weekEnd)
            } ?: false

            if (isSelectedInWeek) {
                YearMonth.from(state.selectedDate)
            } else {
                YearMonth.from(weekStart)
            }
        }
        if (state.viewedMonth != currentViewedMonth) {
            onStateChange(state.copy(viewedMonth = currentViewedMonth))
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = state.isExpanded,
            transitionSpec = {
                (fadeIn() + expandVertically(expandFrom = Alignment.Top))
                    .togetherWith(fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top))
            },
            label = "CalendarAnimation"
        ) { isExpanded ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                userScrollEnabled = isExpanded
            ) { page ->
                val diff = (page - INITIAL_PAGE).toLong()
                if (isExpanded) {
                    val month = initialMonth.plusMonths(diff)
                    MonthContent(
                        yearMonth = month,
                        selectedDate = state.selectedDate,
                        onDateClick = { date ->
                            onStateChange(state.copy(selectedDate = date))
                        },
                        contentPadding = contentPadding
                    )
                } else {
                    val weekStart = initialWeek.plusWeeks(diff)
                    CalendarWeekContent(
                        weekStartDate = weekStart,
                        selectedDate = state.selectedDate,
                        onDateClick = { date ->
                            onStateChange(state.copy(selectedDate = date))
                        },
                        contentPadding = contentPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthContent(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    contentPadding: PaddingValues
) {
    DaysGrid(
        startDate = yearMonth.atDay(1).atStartOfWeek(),
        rowCount = 6,
        currentMonth = yearMonth,
        selectedDate = selectedDate,
        onDateClick = onDateClick,
        contentPadding = contentPadding
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarWithPagerPreview() {
    AppTheme {
        var state by remember { mutableStateOf(CalendarState(selectedDate = LocalDate.now())) }
        CalendarWithPager(
            state = state,
            onStateChange = { state = it }
        )
    }
}


@Composable
fun CalendarWithPager(
    modifier: Modifier = Modifier,
    state: CalendarState,
    onStateChange: (CalendarState) -> Unit
) {

    val corutineScope = rememberCoroutineScope()

    val today = remember { LocalDate.now() }
    val contentPagerState = rememberPagerState(
        initialPage = INITIAL_PAGE + ChronoUnit.DAYS.between(today, state.selectedDate ?: today).toInt()
    ) { PAGE_COUNT }

    LaunchedEffect(state.selectedDate) {
        state.selectedDate?.let { date ->
            val targetPage = INITIAL_PAGE + ChronoUnit.DAYS.between(today, date).toInt()
            if (contentPagerState.currentPage != targetPage) {
                corutineScope.launch {
                    contentPagerState.animateScrollToPage(targetPage)
                }

            }
        }
    }

    LaunchedEffect(contentPagerState.currentPage) {
        val date = today.plusDays((contentPagerState.currentPage - INITIAL_PAGE).toLong())
        if (state.selectedDate != date) {
            onStateChange(state.copy(selectedDate = date))
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        Calendar(
            state = state,
            onStateChange = onStateChange
        )
        HorizontalPager(
            state = contentPagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val date = today.plusDays((page - INITIAL_PAGE).toLong())
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
