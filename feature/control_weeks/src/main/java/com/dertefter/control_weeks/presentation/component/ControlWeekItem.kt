package com.dertefter.control_weeks.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@Composable
fun ControlWeekItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    week: String,
    isTop: Boolean = true,
    isBottom: Boolean = true,
) {

    val topRounding = if (isTop) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small
    val bottomRounding  = if (isBottom) MaterialTheme.rounding.largeIncreased else MaterialTheme.rounding.small

    val shape = MaterialTheme.cornerShape(
        topStart = topRounding,
        topEnd = topRounding,
        bottomStart = bottomRounding,
        bottomEnd = bottomRounding
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .weight(1f),

            )
            {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = week,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMediumEmphasized,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
    }
}

@Preview
@Composable
fun ControlWeekItemPreview() {
    AppTheme {
        ControlWeekItem(
            title = "Математический анализ",
            week = "5-ая неделя",
            value = "2"
        )
    }
}
