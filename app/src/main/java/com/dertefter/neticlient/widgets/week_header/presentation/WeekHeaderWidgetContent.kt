package com.dertefter.neticlient.widgets.week_header.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.background
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.TextAlign
import com.dertefter.neticlient.R

@Composable
fun WeekHeaderWidgetContent(
    weekHeader: String?,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .cornerRadius(16.dp)
            .fillMaxSize()
            .background(GlanceTheme.colors.tertiaryContainer)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = weekHeader ?: context.getString(R.string.loading),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = GlanceTheme.colors.onTertiaryContainer
            )
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(
    heightDp = 100,
    widthDp = 200,
)
@Composable
fun WeekHeaderWidgetPreview() {
    GlanceTheme {
        WeekHeaderWidgetContent(weekHeader = "3 учебная неделя")
    }
}
