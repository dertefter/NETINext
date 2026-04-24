package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun DaysGrid(
    startDate: LocalDate,
    rowCount: Int,
    currentMonth: YearMonth?,
    selectedDate: LocalDate?,
    onDateClick: (LocalDate) -> Unit,
    contentPadding: PaddingValues
) {
    val today = remember { LocalDate.now() }
    Column(modifier = Modifier.padding(contentPadding).fillMaxWidth()) {
        DaysOfWeekHeader()
        repeat(rowCount) { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { column ->
                    val date = startDate.plusDays((row * 7 + column).toLong())
                    Box(Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                        DayCell(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == today,
                            isCurrentMonth = currentMonth == null || YearMonth.from(date) == currentMonth,
                            onDateClick = onDateClick
                        )
                    }
                }
            }
        }
    }
}