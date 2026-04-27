package com.dertefter.neticlient.widgets.near_schedule.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import java.time.LocalDate

@Composable
fun TimeSlotItem(timeSlot: TimeSlotDto) {

    Box(
        modifier = GlanceModifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    ) {

        Column(
            modifier = GlanceModifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timeSlot.startTimeString,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.onBackground
                ),
                modifier = GlanceModifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = GlanceModifier
                    .background(GlanceTheme.colors.onSurfaceVariant)
                    .width(1.dp)
                    .defaultWeight()
            ) {}


            Text(
                text = timeSlot.endTimeString,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = GlanceTheme.colors.onBackground
                ),
                modifier = GlanceModifier.padding(top = 4.dp)
            )
        }

        Column(
            modifier = GlanceModifier
                .padding(start = 40.dp)
                .fillMaxWidth()
        ) {
            timeSlot.lessons.forEachIndexed { index, lesson ->
                LessonItem(lesson)
                if (index < timeSlot.lessons.size - 1 && index != timeSlot.lessons.lastIndex) {
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalGlancePreviewApi::class)
@Composable
@Preview(widthDp = 400)
fun TimeSlotItemPreview(){
    val sampleTimeSlot = TimeSlotDto(
        dateString = LocalDate.now().toString(),
        startTimeString = "08:30",
        endTimeString = "10:00",
        lessons = listOf(
            LessonDto(
                name = "Высшая математика",
                type = "Лекция",
                aud = "301",
                persons = null
            ),
            LessonDto(
                name = "Физика",
                type = "Практика",
                aud = "202",
                persons = null
            )
        )
    )

    GlanceTheme {
        TimeSlotItem(timeSlot = sampleTimeSlot)
    }
}
