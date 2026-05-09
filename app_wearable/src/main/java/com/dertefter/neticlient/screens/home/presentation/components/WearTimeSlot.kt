package com.dertefter.neticlient.screens.home.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.CardDefaults
import androidx.wear.compose.material3.LinearProgressIndicator
import androidx.wear.compose.material3.LinearProgressIndicatorDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

interface TimeSlotScope {
    val isNow: Boolean
    val isPast: Boolean
    val isFuture: Boolean
}


@Composable
fun WearTimeSlot(
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
    startTime: LocalTime,
    endTime: LocalTime,
    transformation: SurfaceTransformation? = null,
    content: @Composable TimeSlotScope.() -> Unit
) {
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val isToday = remember(date) { date == LocalDate.now() }

    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val isNow = remember(tick, isToday, startTime, endTime) {
        isToday && !LocalTime.now().isBefore(startTime) && !LocalTime.now().isAfter(endTime)
    }

    val isPast = remember(tick, date, isToday, endTime) {
        date.isBefore(LocalDate.now()) || (isToday && LocalTime.now().isAfter(endTime))
    }

    val isFuture = remember(tick, date, isToday, startTime) {
        date.isAfter(LocalDate.now()) || (isToday && LocalTime.now().isBefore(startTime))
    }

    val scope = remember(isNow, isPast, isFuture) {
        object : TimeSlotScope {
            override val isNow = isNow
            override val isPast = isPast
            override val isFuture = isFuture
        }
    }

    LaunchedEffect(isToday, startTime, endTime) {
        while (true) {
            tick = System.currentTimeMillis()
            delay(2000)
        }
    }

    val progressLambda = remember(startTime, endTime, isToday) {
        {
            tick
            val now = LocalTime.now()
            when {
                !isToday || now.isBefore(startTime) -> 0f
                now.isAfter(endTime) -> 1f
                else -> {
                    val totalSeconds = Duration.between(startTime, endTime).seconds.toFloat()
                    val passedSeconds = Duration.between(startTime, now).seconds.toFloat()
                    (passedSeconds / totalSeconds).coerceIn(0f, 1f)
                }
            }
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isNow)
            MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer,
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isNow)
            MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface,
        label = "textColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors().copy(
            containerColor = backgroundColor
        ),
        transformation = transformation
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = startTime.format(timeFormatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )

                LinearProgressIndicator(
                    progress = progressLambda,
                    modifier = Modifier.weight(1f),
                    colors = ProgressIndicatorDefaults.colors().copy(
                        indicatorColor = textColor,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    strokeWidth = LinearProgressIndicatorDefaults.StrokeWidthSmall,
                )

                Text(
                    text = endTime.format(timeFormatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                scope.content()
            }
        }
    }

}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun TimeSlotPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            WearTimeSlot(
                startTime = LocalTime.now().minusMinutes(30),
                endTime = LocalTime.now().plusMinutes(30),
                date = LocalDate.now().plusDays(1),
                content = {
                    Text(
                        text = "Высшая математика",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
