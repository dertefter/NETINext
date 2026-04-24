package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
fun CalendarWeekContent(
    weekStartDate: LocalDate,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    contentPadding: PaddingValues
) {
    DaysGrid(
        startDate = weekStartDate,
        rowCount = 1,
        currentMonth = null,
        selectedDate = selectedDate,
        onDateClick = onDateClick,
        contentPadding = contentPadding
    )
}