package com.dertefter.home.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.design.R
import com.dertefter.design.components.schedule.GroupButton
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.home.presentation.Event
import com.dertefter.home.presentation.ScheduleState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleHeader(
    scheduleState: ScheduleState,
    onEvent: (Event) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    val targetDate: LocalDate? = scheduleState.date
    val nowDate = LocalDate.now()
    val diff: Long? = if (targetDate == null) null else ChronoUnit.DAYS.between(nowDate, targetDate)
    val primaryLine: String? = when (diff) {
        null -> "Расписание занятий"
        0L -> stringResource(R.string.design_pretty_date_today)
        1L -> stringResource(R.string.design_pretty_date_tomorrow)
        2L -> stringResource(R.string.design_pretty_date_after_tomorrow)
        in 3L..6L -> pluralStringResource(R.plurals.pretty_date_days, diff.toInt(), diff.toInt())
        else -> null
    }
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", LocalLocale.current.platformLocale)
    val secondaryLine = targetDate?.format(formatter)?.replaceFirstChar { it.uppercase() }

    val title = primaryLine ?: (secondaryLine ?: "")
    val subtitle: String? = if (title == secondaryLine) null else secondaryLine

    Row(
        modifier = modifier
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.defaultScreenPadding
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMediumEmphasized
                )
            }

        }

        if (!scheduleState.group?.name.isNullOrEmpty()) {
            GroupButton(
                onClick = {
                    onEvent(Event.OnNavigateToSearchGroup)
                },
                group = scheduleState.group.name,
                isIndividual = scheduleState.group.isIndividual,
                isLoading = scheduleState.isLoading
            )
        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ScheduleHeaderPreviewNoGroup() {
    AppTheme {
        ScheduleHeader(
            scheduleState = ScheduleState(
                group = GroupDto("d")
            ), onEvent = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ScheduleHeaderPreviewWithGroup() {
    AppTheme {
        ScheduleHeader(
            scheduleState = ScheduleState(
                date = LocalDate.now(),  group = GroupDto("d"), isLoading = false
            ), onEvent = {})
    }
}
