package com.dertefter.calendar.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.dertefter.calendar.presentation.components.LessonItem
import com.dertefter.calendar.presentation.components.PrettyDate
import com.dertefter.calendar.presentation.components.WearTimeSlot
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.icons.Icons
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    uiState: ScheduleState
) {
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    val dates = remember {
        val today = LocalDate.now()
        (-30..365).map { today.plusDays(it.toLong()) }
    }

    val listItems = remember(dates, uiState.timeSlots) {
        val items = mutableListOf<ScheduleListItem>()
        dates.forEach { date ->
            items.add(ScheduleListItem.Header(date))
            val daySlots = uiState.timeSlots[date]
            if (daySlots.isNullOrEmpty()) {
                items.add(ScheduleListItem.Empty(date))
            } else {
                daySlots.forEach { slot ->
                    items.add(ScheduleListItem.Slot(slot))
                }
            }
        }
        items
    }

    LaunchedEffect(listItems) {
        val todayIndex = listItems.indexOfFirst {
            when (it) {
                is ScheduleListItem.Header -> it.date == LocalDate.now()
                else -> false
            }
        }
        if (todayIndex != -1) {
            listState.scrollToItem(todayIndex)
        }
    }

    AppScaffold {
        ScreenScaffold(
            scrollState = listState
        ) { contentPadding ->
            TransformingLazyColumn(
                contentPadding = contentPadding,
                state = listState
            ) {
                items(listItems.size) { index ->
                    when (val item = listItems[index]) {
                        is ScheduleListItem.Header -> {
                            ListHeader(
                                modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            ) {
                                PrettyDate(date = item.date)
                            }
                        }
                        is ScheduleListItem.Empty -> {
                            ListHeader(
                                modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                                transformation = SurfaceTransformation(transformationSpec),
                            ) {
                                Text(
                                    text = "Нет занятий",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        is ScheduleListItem.Slot -> {
                            val timeSlot = item.slot
                            WearTimeSlot(
                                modifier = Modifier.transformedHeight(this, transformationSpec),
                                startTime = timeSlot.getStartTime(),
                                endTime = timeSlot.getEndTime(),
                                date = timeSlot.getDate(),
                                transformation = SurfaceTransformation(transformationSpec)
                            ) {
                                val lessons = timeSlot.lessons
                                var currentIndex by rememberSaveable(timeSlot) { mutableIntStateOf(0) }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AnimatedContent(
                                        targetState = currentIndex,
                                        modifier = Modifier.weight(1f),
                                        label = "lesson_switch",
                                    ) { i ->
                                        val lesson = lessons.getOrNull(i)
                                        if (lesson != null) {
                                            LessonItem(
                                                title = lesson.name,
                                                aud = lesson.aud,
                                                type = lesson.type,
                                                isHighlight = isNow,
                                                personIds = lesson.persons?.map { it.personId } ?: emptyList(),
                                            )
                                        }
                                    }

                                    if (lessons.size > 1) {
                                        Box(
                                            modifier = Modifier
                                                .width(32.dp)
                                                .fillMaxHeight()
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                                .clickable {
                                                    currentIndex = (currentIndex + 1) % lessons.size
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.MoreVert,
                                                contentDescription = "Next lesson",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class ScheduleListItem {
    data class Header(val date: LocalDate) : ScheduleListItem()
    data class Slot(val slot: TimeSlotDto) : ScheduleListItem()
    data class Empty(val date: LocalDate) : ScheduleListItem()
}
