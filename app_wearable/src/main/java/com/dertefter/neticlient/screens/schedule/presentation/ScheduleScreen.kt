package com.dertefter.neticlient.screens.schedule.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.neticlient.R
import com.dertefter.neticlient.screens.home.presentation.components.LessonItem
import com.dertefter.neticlient.screens.home.presentation.components.PrettyDate
import com.dertefter.neticlient.screens.home.presentation.components.WearTimeSlot
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    scheduleState: ScheduleState,
    onEvent: (ScheduleEvent) -> Unit
) {
    AppScaffold {
        if (scheduleState.group == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Группа не выбрана",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Выберите группу в мобильном приложении",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (scheduleState.isLoading && scheduleState.dates.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AppLoadingIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (scheduleState.error != null && scheduleState.dates.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.wear_loading_error), textAlign = TextAlign.Center)
            }
        } else if (scheduleState.dates.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.wear_no_lessons), textAlign = TextAlign.Center)
            }
        } else {
            val initialPage = remember(scheduleState.dates) {
                val today = LocalDate.now()
                val index = scheduleState.dates.indexOfFirst { it >= today }
                if (index != -1) index else 0
            }

            val pagerState = rememberPagerState(
                initialPage = initialPage,
                pageCount = { scheduleState.dates.size }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                val date = scheduleState.dates[pageIndex]
                val timeSlots = scheduleState.scheduleByDate[date] ?: emptyList()
                DayScheduleContent(
                    date = date,
                    groupName = scheduleState.group.name,
                    timeSlots = timeSlots
                )
            }
        }
    }
}

@Composable
fun DayScheduleContent(
    date: LocalDate,
    groupName: String,
    timeSlots: List<TimeSlotDto>
) {
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(
        scrollState = listState
    ) { contentPadding ->
        TransformingLazyColumn(
            contentPadding = contentPadding,
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = groupName.uppercase())
                        PrettyDate(date = date)
                    }
                }
            }

            items(timeSlots.size) { index ->
                val timeSlot = timeSlots[index]

                WearTimeSlot(
                    modifier = Modifier.transformedHeight(this, transformationSpec),
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
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
