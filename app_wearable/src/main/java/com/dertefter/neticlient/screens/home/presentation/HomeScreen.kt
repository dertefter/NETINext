package com.dertefter.neticlient.screens.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.tooling.preview.devices.WearDevices
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.neticlient.R
import com.dertefter.neticlient.screens.home.presentation.components.LessonItem
import com.dertefter.neticlient.screens.home.presentation.components.PrettyDate
import com.dertefter.neticlient.screens.home.presentation.components.WearTimeSlot
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun HomeScreen(
    scheduleState: ScheduleState,
    onEvent: (HomeEvent) -> Unit,
) {
    AppScaffold {
        val listState = rememberTransformingLazyColumnState()
        val transformationSpec = rememberTransformationSpec()

        LaunchedEffect(scheduleState.timeSlots) {
            while (true) {
                val now = LocalTime.now()
                val today = LocalDate.now()
                val index = scheduleState.timeSlots.indexOfFirst { slot ->
                    slot.getDate() == today && !now.isBefore(slot.getStartTime()) && !now.isAfter(slot.getEndTime())
                }
                if (index != -1) {
                    listState.animateScrollToItem(index + 1)
                }
                delay(10000)
            }
        }

        ScreenScaffold(
            scrollState = listState,
            edgeButton = {
                if (scheduleState.group != null){
                    EdgeButton(
                        onClick = { onEvent(HomeEvent.OnOpenDetails) },
                        buttonSize = EdgeButtonSize.Small
                    ) {
                        Text(
                            text = "Подробнее",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
               
            }
        ) { contentPadding ->
            TransformingLazyColumn(contentPadding = contentPadding, state = listState) {
                if (scheduleState.group == null) {
                    item {
                        Text(
                            text = "Группа не выбрана",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(
                                    top = LocalWindowInfo.current.containerDpSize.height / 7
                                )
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    item {
                        Text(
                            text = "Выберите группу в мобильном приложении",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else if (scheduleState.timeSlots.isEmpty()) {
                    item {
                        ListHeader(
                            modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = scheduleState.group.name)
                                Text(
                                    text = stringResource(R.string.wear_home_title),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }

                        }
                    }
                    if (scheduleState.isLoading) {
                        item {
                            AppLoadingIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (scheduleState.error != null) {
                        item {
                            Text(
                                text = stringResource(R.string.wear_loading_error),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        item {
                            Text(
                                text = stringResource(R.string.wear_no_lessons),
                                textAlign = TextAlign.Center
                            )
                        }
                    }



                }
                else {
                    item {
                        ListHeader(
                            modifier = Modifier.fillMaxWidth().transformedHeight(this, transformationSpec),
                            transformation = SurfaceTransformation(transformationSpec),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = scheduleState.group.name.uppercase())
                                scheduleState.date?.let { date ->
                                    PrettyDate(
                                        date = date
                                    )
                                }

                            }
                        }
                    }

                    items(scheduleState.timeSlots.size) { index ->
                        val timeSlot = scheduleState.timeSlots[index]

                        WearTimeSlot(
                            modifier = Modifier
                                .transformedHeight(this, transformationSpec),
                            startTime = timeSlot.getStartTime(),
                            endTime = timeSlot.getEndTime(),
                            date = timeSlot.getDate(),
                            transformation = SurfaceTransformation(transformationSpec),
                            content = {
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
                                                tint =MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            },
                        )

                    }
                }
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val scheduleState = ScheduleState(
        group = GroupDto("ФБТХ-32"),
        timeSlots = listOf(
            TimeSlotDto(
                dateString = LocalDate.now().toString(),
                startTimeString = "08:30",
                endTimeString = "10:00",
                lessons = listOf(
                    LessonDto(
                        name = "Высшая математика",
                        type = "Лекция",
                        aud = "301",
                        persons = emptyList()
                    ),
                    LessonDto(
                        name = "Высшая математика",
                        type = "Лекция",
                        aud = "301",
                        persons = emptyList()
                    )
                )
            ),
            TimeSlotDto(
                dateString = LocalDate.now().toString(),
                startTimeString = "10:15",
                endTimeString = "11:45",
                lessons = listOf(
                    LessonDto(
                        name = "Физика",
                        type = "Практика",
                        aud = "202",
                        persons = emptyList()
                    )
                )
            )
        )
    )
    HomeScreen(
        scheduleState = scheduleState,
        onEvent = {}
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun HomeScreenPreview2() {
    val scheduleState = ScheduleState(
        group = null,
        isLoading = false
    )
    HomeScreen(
        scheduleState = scheduleState,
        onEvent = {}
    )
}

@Preview(device = WearDevices.LARGE_ROUND, showSystemUi = true)
@Composable
fun HomeScreenPreview3() {
    val scheduleState = ScheduleState(
        group = null,
        isLoading = false
    )
    HomeScreen(
        scheduleState = scheduleState,
        onEvent = {}
    )
}


