package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.style.TextAlign
import java.time.DayOfWeek
import java.time.format.TextStyle

@Composable
fun DaysOfWeekHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        DayOfWeek.entries.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day.getDisplayName(TextStyle.SHORT, LocalLocale.current.platformLocale),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMediumEmphasized,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}