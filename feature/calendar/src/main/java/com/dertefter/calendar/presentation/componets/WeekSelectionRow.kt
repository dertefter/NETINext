package com.dertefter.calendar.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.UiState
import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import com.dertefter.design.components.schedule.WeekItem
import com.dertefter.design.theme.spacing
import java.time.LocalDate

@Composable
fun WeekSelectionRow(
    uiState: UiState,
    calendarState: CalendarState,
    onCalendarStateChange: (CalendarState) -> Unit,
    onWeekSelectionVisibilityChange: (Boolean) -> Unit,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekListState = rememberLazyListState()
    val today = remember { LocalDate.now() }

    val selectedWeekNumber = remember(calendarState.selectedDate, uiState.weekBounds) {
        val date = calendarState.selectedDate ?: today
        uiState.weekBounds.find { week ->
            !date.isBefore(week.getStartDate()) && !date.isAfter(week.getEndDate())
        }?.weekNumber ?: 0
    }

    LaunchedEffect(selectedWeekNumber) {
        val index = uiState.weekBounds.indexOfFirst { it.weekNumber == selectedWeekNumber }
        if (index >= 0) weekListState.animateScrollToItem(index)
    }

    LazyRow(
        state = weekListState,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        items(uiState.weekBounds) { week ->
            WeekItem(
                weekNumber = week.weekNumber,
                isSelected = week.weekNumber == selectedWeekNumber,
                isCurrentWeek = !today.isBefore(week.getStartDate()) &&
                        !today.isAfter(week.getEndDate()),
                onClick = {
                    val targetDate = if (today in week.getStartDate()..week.getEndDate()) {
                        today
                    } else {
                        week.getStartDate()
                    }
                    onCalendarStateChange(calendarState.copy(selectedDate = targetDate))
                    onWeekSelectionVisibilityChange(false)
                }
            )
        }
    }
}
