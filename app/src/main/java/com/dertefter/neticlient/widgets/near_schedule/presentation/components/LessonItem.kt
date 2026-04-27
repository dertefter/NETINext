package com.dertefter.neticlient.widgets.near_schedule.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.dertefter.data.dto.schedule.LessonDto

@Composable
fun LessonItem(lesson: LessonDto) {
    Column(modifier = GlanceModifier
        .cornerRadius(6.dp)
        .background(GlanceTheme.colors.secondaryContainer)
        .padding(8.dp)
        .fillMaxWidth()
    ) {
        Text(
            text = lesson.name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = GlanceTheme.colors.onSecondaryContainer
            ),
            maxLines = 2
        )
        Box(
            modifier = GlanceModifier
                .padding(top = 2.dp)
                .fillMaxWidth()
        ) {
            lesson.type?.let {
                Text(
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    text = it,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSecondaryContainer,
                        textAlign = TextAlign.Start
                    ),
                )
            }

            lesson.aud?.let {
                Text(
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    text = it,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = GlanceTheme.colors.onSecondaryContainer,
                        textAlign = TextAlign.End
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
@Preview(widthDp = 300, heightDp = 100)
fun LessonItemPreview() {
    val sampleLesson = LessonDto(
        name = "Высшая математика",
        type = "Лекция",
        aud = "301",
        persons = null
    )
    GlanceTheme {
        LessonItem(lesson = sampleLesson)
    }
}
