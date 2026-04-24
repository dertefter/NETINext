package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.calendar.R
import com.dertefter.design.theme.AppTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonthTitle(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth?,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineSmallEmphasized
) {
    val currentYearMonth = remember { YearMonth.now() }
    val monthName = yearMonth?.month?.getDisplayName(
        TextStyle.FULL_STANDALONE, LocalLocale.current.platformLocale
    )
        ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString() }

    val text = if (yearMonth == null) {
        null
    } else if (currentYearMonth.year != yearMonth.year) {
        "$monthName, ${yearMonth.year}"
    } else {
        monthName
    }

    Text(
        text = text ?: stringResource(R.string.calendar), style = style, modifier = modifier
    )
}

