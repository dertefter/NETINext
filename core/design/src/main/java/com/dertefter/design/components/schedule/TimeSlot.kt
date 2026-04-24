package com.dertefter.design.components.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.loading.RotatedLinearWavyProgressIndicator
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimeSlot(
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
    startTime: LocalTime,
    endTime: LocalTime,
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

    val spacing = MaterialTheme.spacing.medium
    val timeColumnWidth = 56.dp

    Layout(
        modifier = modifier.fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.small)
                    .width(timeColumnWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = startTime.format(timeFormatter),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                RotatedLinearWavyProgressIndicator(
                    progress = progressLambda,
                    modifier = Modifier
                        .weight(1f)
                )


                Text(
                    text = endTime.format(timeFormatter),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                scope.content()
            }
        }
    ) { measurables, constraints ->
        val timeMeasurable = measurables[0]
        val contentMeasurable = measurables[1]

        val timeWidthPx = timeColumnWidth.roundToPx()
        val spacingPx = spacing.roundToPx()

        val contentConstraints = constraints.copy(
            minWidth = 0,
            maxWidth = (constraints.maxWidth - timeWidthPx - spacingPx).coerceAtLeast(0)
        )
        val contentPlaceable = contentMeasurable.measure(contentConstraints)

        val timePlaceable = timeMeasurable.measure(
            Constraints.fixed(timeWidthPx, contentPlaceable.height)
        )

        layout(constraints.maxWidth, contentPlaceable.height) {
            timePlaceable.placeRelative(0, 0)
            contentPlaceable.placeRelative(timeWidthPx + spacingPx, 0)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TimeSlotPreview() {
    AppTheme {
        Column {
            TimeSlot(
                startTime = LocalTime.now().minusMinutes(60),
                endTime = LocalTime.now().plusMinutes(60)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isNow) MaterialTheme.colorScheme.primaryContainer 
                            else MaterialTheme.colorScheme.surfaceVariant, 
                            MaterialTheme.shapes.medium
                        )
                        .padding(100.dp)
                ) {
                    Text(
                        text = when {
                            isNow -> "Сейчас идет"
                            isPast -> "Уже прошло"
                            else -> "Будет позже"
                        },
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
