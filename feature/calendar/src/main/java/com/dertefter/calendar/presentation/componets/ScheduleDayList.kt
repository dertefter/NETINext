package com.dertefter.calendar.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.dertefter.calendar.R
import com.dertefter.calendar.presentation.Event
import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.EventDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.components.schedule.NoLessons
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ScheduleDayList(
    timeSlots: List<TimeSlotDto>,
    sessiaTimeSlots: List<TimeSlotDto>,
    isGroupSelected: Boolean,
    isError: AppError? = null,
    contentPadding: PaddingValues,
    onStateCreated: (LazyListState) -> Unit,
    onStateDisposed: () -> Unit,
    onEvent: (Event) -> Unit,
    isEventsShowed: Boolean,
    isScheduleShowed: Boolean,
    events: List<EventDto>
) {
    val listState = rememberLazyListState()

    DisposableEffect(listState) {
        onStateCreated(listState)
        onDispose { onStateDisposed() }
    }

    LazyColumn(
        contentPadding = contentPadding,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
            .fillMaxSize(),
    ) {
        item { Spacer(Modifier.height(MaterialTheme.spacing.small)) }

        item {
            when {
                !isGroupSelected -> NoLessons(
                    text = stringResource(R.string.no_group_selected),
                    subtext = stringResource(R.string.tap_to_add_group),
                    onClick = {
                        onEvent(Event.OnOpenGroupSearch)
                    }
                )
                timeSlots.isEmpty() && sessiaTimeSlots.isEmpty() && isError == null -> {
                    NoLessons(text = stringResource(R.string.no_classes))
                }
            }
        }


        if (isScheduleShowed){
            timeSlotsItems(
                timeSlots = timeSlots,
                onEvent = onEvent
            )
        }

        timeSlotsItems(
            timeSlots = sessiaTimeSlots,
            onEvent = onEvent
        )

        if (isEventsShowed && events.isNotEmpty()) {
            item {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "События",
                        modifier = Modifier
                            .padding(
                                vertical = MaterialTheme.spacing.medium,
                                horizontal = MaterialTheme.rounding.large
                            )
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.titleLargeEmphasized
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier
                            .clip(
                                MaterialTheme.shapes.largeIncreased
                            )
                            .fillMaxWidth()
                    ) {
                        for (eventItem in events){
                            EventItem(
                                title = eventItem.title
                            )
                        }
                    }
                }

            }
        }

        item { Spacer(Modifier.height(MaterialTheme.spacing.small)) }
    }
}