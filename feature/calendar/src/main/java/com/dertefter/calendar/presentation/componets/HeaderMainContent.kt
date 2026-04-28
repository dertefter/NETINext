package com.dertefter.calendar.presentation.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.UiState
import com.dertefter.calendar.presentation.componets.calendar.MonthTitle
import com.dertefter.design.components.schedule.GroupButton
import com.dertefter.design.components.schedule.WeekItem
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.spacing
import com.dertefter.design.theme.AppTheme
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import java.time.YearMonth

import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import java.time.LocalDate

@Composable
fun HeaderMainContent(
    uiState: UiState,
    calendarState: CalendarState,
    onCalendarStateChange: (CalendarState) -> Unit,
    onWeekSelectionVisibilityChange: (Boolean) -> Unit,
    onEvent: (Event) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onCalendarStateChange(
                    calendarState.copy(isExpanded = !calendarState.isExpanded)
                )
            }
        ) {
            MonthTitle(
                yearMonth = calendarState.viewedMonth,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
            Icon(
                imageVector = if (calendarState.isExpanded)
                    Icons.ArrowDropUp else Icons.ArrowDropDown,
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        GroupButton(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.small)
                .weight(10f, fill = false),
            group = uiState.group?.name,
            isIndividual = uiState.group?.isIndividual ?: false,
            isLoading = uiState.isUpdating,
            onClick = {
                onEvent(Event.OnOpenGroupSearch)
            }
        )

        if (uiState.weekBounds.isNotEmpty()) {
            val selectedDate = calendarState.selectedDate ?: LocalDate.now()
            val selectedWeek = uiState.weekBounds.find { week ->
                !selectedDate.isBefore(week.getStartDate()) && !selectedDate.isAfter(week.getEndDate())
            }
            WeekItem(
                weekNumber = selectedWeek?.weekNumber ?: 0,
                isSelected = true,
                onClick = { onWeekSelectionVisibilityChange(true) }
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=650px,height=2340px,dpi=440")
@Composable
fun HeaderMainContentPreview() {
    AppTheme {
        HeaderMainContent(
            uiState = UiState(
                group = GroupDto(name = "АБ-123"),
                weekBounds = listOf(
                    WeekBoundsDto(startDateString = LocalDate.now().toString(), weekNumber = 8)
                )
            ),
            calendarState = CalendarState(
                selectedDate = LocalDate.now(),
                viewedMonth = YearMonth.now(),
                isExpanded = false
            ),
            onCalendarStateChange = {},
            onWeekSelectionVisibilityChange = {},
            onEvent = {}
        )
    }
}
