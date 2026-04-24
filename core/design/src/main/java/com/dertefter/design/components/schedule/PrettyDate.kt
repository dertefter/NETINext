package com.dertefter.design.components.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dertefter.design.R
import com.dertefter.design.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                0L -> stringResource(R.string.pretty_date_today)
                1L -> stringResource(R.string.pretty_date_tomorrow)
                2L -> stringResource(R.string.pretty_date_after_tomorrow)
                in 3L..6L -> pluralStringResource(R.plurals.pretty_date_days, diff.toInt(), diff.toInt())
                else -> null
            }

            val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", LocalLocale.current.platformLocale)
            val secondaryLine = targetDate.format(formatter).replaceFirstChar { it.uppercase() }

            if (primaryLine != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    text = primaryLine
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    text = secondaryLine
                )
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    text = secondaryLine
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
fun PrettyDatePreview1(){
    AppTheme {
        PrettyDate(
            date = LocalDate.now(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrettyDatePreview2(){
    AppTheme {
        PrettyDate(
            date = LocalDate.now().plusDays(4),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrettyDatePreview3(){
    AppTheme {
        PrettyDate(
            date = LocalDate.now().plusDays(14),
        )
    }
}

