package com.dertefter.calendar.presentation.componets

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.calendar.presentation.Event
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.components.schedule.TimeSlot
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LazyListScope.timeSlotsItems(
    timeSlots: List<TimeSlotDto> = emptyList(),
    onEvent: (Event) -> Unit = {},
) {



    itemsIndexed(timeSlots, key = { _, timeSlot -> timeSlot.hashCode() }) { index, timeSlot ->

        val cornerSmall = MaterialTheme.rounding.small
        val cornerLarge = MaterialTheme.rounding.extraLarge

        TimeSlot(
            startTime = timeSlot.getStartTime(),
            endTime = timeSlot.getEndTime(),
            date = timeSlot.getDate(),




            modifier = Modifier.animateItem().clip(
                MaterialTheme.cornerShape(
                    topStart = cornerSmall,
                    bottomStart = cornerSmall,
                    topEnd = if (index == 0) cornerLarge else cornerSmall,
                    bottomEnd = if (index == timeSlots.size - 1) cornerLarge else cornerSmall
                )
            ),

            content = {
                val lessons = timeSlot.lessons
                var currentIndex by rememberSaveable(timeSlot) { mutableIntStateOf(0) }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
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
                                onClick = {
                                    onEvent(
                                        Event.OnOpenLessonDetail(
                                            name = lesson.name,
                                            type = lesson.type,
                                            aud = lesson.aud,
                                            personIds = lesson.persons?.map { it.personId } ?: emptyList(),
                                            startTime = timeSlot.getStartTime(),
                                            endTime = timeSlot.getEndTime(),
                                            date = timeSlot.getDate()
                                        )
                                    )
                                }
                            )
                        }
                    }
                    
                    if (lessons.size > 1) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .fillMaxHeight()
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
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
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimeSlotsItemsPreview() {
    val mockTimeSlots = listOf(
        TimeSlotDto(
            dateString = LocalDate.now().toString(),
            startTimeString = LocalTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            endTimeString = LocalTime.now().plusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            lessons = listOf(
                LessonDto(
                    name = "Высшая математика",
                    type = "Лекция",
                    aud = "404",
                    persons = null
                ),
                LessonDto(
                    name = "Очень длинное название предмета которое точно не влезет в одну строку и заставит карточку расшириться по высоте",
                    type = "Практика",
                    aud = "101",
                    persons = null
                )
            )
        ),
        TimeSlotDto(
            dateString = LocalDate.now().toString(),
            startTimeString = LocalTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            endTimeString = LocalTime.now().plusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            lessons = listOf(
                LessonDto(
                    name = "Высшая математика",
                    type = "Лекция",
                    aud = "404",
                    persons = null
                )
            )
        ),
        TimeSlotDto(
            dateString = LocalDate.now().toString(),
            startTimeString = LocalTime.now().minusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            endTimeString = LocalTime.now().plusHours(1).format(DateTimeFormatter.ISO_LOCAL_TIME),
            lessons = listOf(
                LessonDto(
                    name = "Высшая математика",
                    type = "Лекция",
                    aud = "404",
                    persons = null
                )
            )
        )
    )

    AppTheme {
        LazyColumn(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            timeSlotsItems(mockTimeSlots)
        }
    }
}
