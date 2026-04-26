package com.dertefter.calendar.presentation.componets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.UiState
import com.dertefter.calendar.presentation.componets.calendar.Calendar
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import java.time.LocalDate

@Composable
fun CalendarTopBar(
    modifier: Modifier = Modifier,
    uiState: UiState,
    calendarState: CalendarState,
    onCalendarStateChange: (CalendarState) -> Unit,
    isWeekSelectionVisible: Boolean,
    onWeekSelectionVisibilityChange: (Boolean) -> Unit,
    onEvent: (Event) -> Unit,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.defaultScreenPadding)
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        )
        {
            AnimatedContent(
                targetState = isWeekSelectionVisible,
                modifier = Modifier.weight(1f),
                transitionSpec = { (slideInHorizontally() + fadeIn()) togetherWith fadeOut() },
                label = "topBarContent"
            ) { isWeekVisible ->
                if (isWeekVisible) {
                    WeekSelectionRow(
                        uiState = uiState,
                        calendarState = calendarState,
                        onCalendarStateChange = onCalendarStateChange,
                        onWeekSelectionVisibilityChange = onWeekSelectionVisibilityChange,
                        onEvent = onEvent,
                    )
                } else {
                    HeaderMainContent(
                        uiState = uiState,
                        calendarState = calendarState,
                        onCalendarStateChange = onCalendarStateChange,
                        onWeekSelectionVisibilityChange = onWeekSelectionVisibilityChange,
                        onEvent = onEvent
                    )
                }
            }
        }

        if (uiState.isError != null) {
            ErrorCard(
                title = "Не удалось обновить расписание",
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                onRetry = {
                    onEvent(Event.OnUpdateSchedule)
                }
            )
        }

        Calendar(
            state = calendarState,
            onStateChange = onCalendarStateChange,
            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.defaultScreenPadding)
        )
    }
}

@Preview(showBackground = true, device = "spec:width=800px,height=2340px,dpi=440")
@Composable
private fun CalendarTopBarPreview() {
    AppTheme {
        CalendarTopBar(
            uiState = UiState(
                group = GroupDto(name = "АБ-123"),
                weekBounds = listOf(
                    WeekBoundsDto(startDateString = LocalDate.now().toString(), weekNumber = 8)
                )
            ),
            calendarState = CalendarState(),
            onCalendarStateChange = {},
            isWeekSelectionVisible = false,
            onWeekSelectionVisibilityChange = {},
            onEvent = {},
        )
    }
}
