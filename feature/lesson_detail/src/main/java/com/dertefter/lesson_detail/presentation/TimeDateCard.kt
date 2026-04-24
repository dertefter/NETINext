package com.dertefter.lesson_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
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
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.R
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimeDateCard(
    startTime: LocalTime,
    endTime: LocalTime,
    date: LocalDate,
    modifier: Modifier = Modifier,
){

    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val isToday = remember(date) { date == LocalDate.now() }

    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    val isPast = remember(tick, date, isToday, endTime) {
        date.isBefore(LocalDate.now()) || (isToday && LocalTime.now().isAfter(endTime))
    }

    val isFuture = remember(tick, date, isToday, startTime) {
        date.isAfter(LocalDate.now()) || (isToday && LocalTime.now().isBefore(startTime))
    }

    val isNow = remember(tick, isToday, startTime, endTime) {
        isToday && !LocalTime.now().isBefore(startTime) && !LocalTime.now().isAfter(endTime)
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
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", LocalLocale.current.platformLocale)
    val diff:  Long = ChronoUnit.DAYS.between(LocalDate.now(), date)

    val dateString: String? = when (diff) {
        0L -> stringResource(R.string.pretty_date_today)
        1L -> stringResource(R.string.pretty_date_tomorrow)
        2L -> stringResource(R.string.pretty_date_after_tomorrow)
        in 3L..6L -> pluralStringResource(R.plurals.pretty_date_days, diff.toInt(), diff.toInt())
        else -> "${stringResource(R.string.in_what)} ${date.format(dateFormatter).replaceFirstChar { it.uppercase() }}"
    }

    val dateLabel = when {
        isPast -> "${stringResource(R.string.ended)} ${dateString?.lowercase()} ${stringResource(R.string.in_what)} ${timeFormatter.format(endTime)}"
        isNow -> stringResource(R.string.going_now)
        else -> "${stringResource(R.string.will_start)} ${dateString?.lowercase()} ${stringResource(R.string.in_what)} ${timeFormatter.format(startTime)}"
    }

    val fontWeight = when {
        isNow -> 800
        else -> 500
    }

    val bgColor = when {
        isPast -> MaterialTheme.colorScheme.surfaceVariant
        isNow -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceContainerHigh
    }

    val contentColor = when {
        isPast -> MaterialTheme.colorScheme.onSurfaceVariant
        isNow -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    LaunchedEffect(isToday, startTime, endTime) {
        while (true) {
            tick = System.currentTimeMillis()
            delay(2000)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        modifier = modifier
            .background(bgColor)
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth()
    ) {
        Text(
            text = dateLabel,
            fontWeight = FontWeight(fontWeight),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ){
            Text(
                text = timeFormatter.format(startTime),
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = FontWeight(fontWeight),
            )

            LinearWavyProgressIndicator(
                progress = progressLambda,
                modifier = Modifier.weight(1f),
                color = contentColor,
                trackColor = contentColor.copy(alpha = 0.4f),
                stopSize = 0.dp
            )
            
            Text(
                text = timeFormatter.format(endTime),
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = FontWeight(fontWeight),
            )
        }

        
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TimeDateCardPreview() {
    AppTheme {
        TimeDateCard(
            startTime = LocalTime.now().minusHours(1),
            endTime = LocalTime.now().plusHours(1),
            date = LocalDate.now()
        )
    }
}


@Preview(showBackground = true, locale = "ru")
@Composable
private fun TimeDateCardPreview2() {
    AppTheme {
        TimeDateCard(
            startTime = LocalTime.now().minusHours(201),
            endTime = LocalTime.now().minusHours(200),
            date = LocalDate.now().minusDays(1)
        )
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
private fun TimeDateCardPreview3() {
    AppTheme {
        TimeDateCard(
            startTime = LocalTime.now().plusHours(1),
            endTime = LocalTime.now().plusHours(10),
            date = LocalDate.now()
        )
    }
}
