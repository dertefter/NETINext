package com.dertefter.home.presentation.content

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.schedule.NoLessons
import com.dertefter.home.R
import com.dertefter.home.presentation.Event
import com.dertefter.home.presentation.ScheduleState
import com.dertefter.design.theme.spacing
import com.dertefter.home.presentation.components.TgLinktCard
import com.dertefter.home.presentation.components.timeSlotsItems


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
fun LazyListScope.scheduleContent(
    scheduleState: ScheduleState,
    onEvent: (Event) -> Unit
) {

    if (scheduleState.isTgShow) {
        item {
            val context = LocalContext.current
            TgLinktCard(
                modifier = Modifier.padding(MaterialTheme.spacing.defaultScreenPadding),
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "https://t.me/neticlient".toUri())
                    context.startActivity(intent)
                },
                onClose = {
                    onEvent(Event.OnHideTg)
                }
            )
        }

    }

    item {
        ScheduleHeader(
            scheduleState = scheduleState,
            onEvent = onEvent,
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.large)
        )
    }

    if (scheduleState.group == null){
        item{
            NoLessons(
                text = stringResource(R.string.home_no_group_selected),
                subtext = stringResource(R.string.home_tap_to_add_group),
                onClick = {
                    onEvent(Event.OnNavigateToSearchGroup)
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
            )
        }
    }

    scheduleState.error?.let{
        item {
            ErrorCard(
                onRetry = {
                    onEvent(Event.OnUpdateSchedule)
                },
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
            )
        }
    }

    timeSlotsItems(
        scheduleState.timeSlots,
        onEvent = onEvent
    )

    if (scheduleState.timeSlots.isEmpty() && !scheduleState.isLoading && scheduleState.error == null && scheduleState.group != null){
        item {
            NoLessons(
                text = stringResource(R.string.home_no_near_lessons),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
            )
        }

    }

}