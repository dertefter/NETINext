package com.dertefter.calendar.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.dertefter.design.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun PrettyDate(
    modifier: Modifier = Modifier,
    date: LocalDate
) {

    val slideInVerticallySpec = MaterialTheme.motionScheme.fastSpatialSpec<IntOffset>()
    val fadeSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()

    AnimatedContent(
        targetState = date,
        modifier = modifier,
        transitionSpec = {
            slideInVertically(
                animationSpec = slideInVerticallySpec
            ) togetherWith fadeOut(animationSpec = fadeSpec)
        },
        label = "PrettyDateAnimation"
    ) { targetDate ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val nowDate = LocalDate.now()
            val diff = ChronoUnit.DAYS.between(nowDate, targetDate)
            val primaryLine: String? = when (diff) {
                0L -> stringResource(R.string.design_pretty_date_today)
                1L -> stringResource(R.string.design_pretty_date_tomorrow)
                2L -> stringResource(R.string.design_pretty_date_after_tomorrow)
                in 3L..6L -> pluralStringResource(R.plurals.pretty_date_days, diff.toInt(), diff.toInt())
                else -> null
            }

            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM")
            val secondaryLine = targetDate.format(formatter).replaceFirstChar { it.uppercase() }

            if (primaryLine != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall,
                    text = primaryLine
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    text = secondaryLine
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    text = secondaryLine
                )
            }
        }
    }
}
