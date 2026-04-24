package com.dertefter.calendar.presentation.componets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dertefter.calendar.R
import com.dertefter.calendar.presentation.Event
import com.dertefter.calendar.presentation.componets.timeSlotsItems
import com.dertefter.data.common.AppError
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.design.components.schedule.NoLessons
import com.dertefter.design.theme.spacing

@Composable
fun ScheduleDayList(
    timeSlots: List<TimeSlotDto>,
    isGroupSelected: Boolean,
    isError: AppError? = null,
    contentPadding: PaddingValues,
    onStateCreated: (LazyListState) -> Unit,
    onStateDisposed: () -> Unit,
    onEvent: (Event) -> Unit,
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

        timeSlotsItems(
            timeSlots = timeSlots,
            onEvent = onEvent
        )

        item {
            when {
                !isGroupSelected -> NoLessons(
                    text = stringResource(R.string.no_group_selected),
                    subtext = stringResource(R.string.tap_to_add_group),
                    onClick = {
                        onEvent(Event.OnOpenGroupSearch)
                    }
                )
                timeSlots.isEmpty() && isError == null -> {
                    NoLessons(text = stringResource(R.string.no_classes))
                }
            }
        }

        item { Spacer(Modifier.height(MaterialTheme.spacing.small)) }
    }
}