package com.dertefter.calendar.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.calendar.R
import com.dertefter.calendar.presentation.componets.CalendarTopBar
import com.dertefter.calendar.presentation.componets.ScheduleDayList
import com.dertefter.calendar.presentation.componets.calendar.CalendarState
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CalendarScreenPhone(
    uiState: UiState,
    onEvent: (Event) -> Unit = {},
) {
    var calendarState by remember { mutableStateOf(CalendarState(selectedDate = LocalDate.now())) }
    var isWeekSelectionVisible by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = remember {
            (calendarState.selectedDate ?: LocalDate.now()).toEpochDay().toInt()
        }
    ) { Int.MAX_VALUE }
    
    val listStates = remember { mutableStateMapOf<Int, LazyListState>() }
    val pagerAlpha = remember { Animatable(0.5f) }

    val spec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()

    LaunchedEffect(Unit) {
        pagerAlpha.animateTo(1f, animationSpec = spec)
    }

    LaunchedEffect(pagerState.currentPage) {
        val dateFromPage = LocalDate.ofEpochDay(pagerState.currentPage.toLong())
        if (calendarState.selectedDate != dateFromPage) {
            calendarState = calendarState.copy(selectedDate = dateFromPage)
        }
    }

    LaunchedEffect(calendarState.selectedDate) {
        val date = calendarState.selectedDate ?: LocalDate.now()
        val targetPage = date.toEpochDay().toInt()
        if (pagerState.currentPage != targetPage) {
            pagerAlpha.snapTo(0.6f)
            pagerState.scrollToPage(targetPage)
            pagerAlpha.animateTo(1f, animationSpec = tween(300))
        }
    }

    LaunchedEffect(calendarState.selectedDate?.year, calendarState.selectedDate?.monthValue) {
        calendarState.selectedDate?.let {
            onEvent(Event.OnUpdateEvents(it.year.toString(), (it.monthValue).toString()))
        }
    }

    val isScrolled by remember {
        derivedStateOf {
            val state = listStates[pagerState.currentPage]
            (state?.firstVisibleItemIndex ?: 0) > 0 || (state?.firstVisibleItemScrollOffset ?: 0) > 0
        }
    }

    val topBarColor by animateColorAsState(
        targetValue = if (isScrolled || calendarState.isExpanded) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "topBarColor"
    )

    val fabElevation by animateDpAsState(
        targetValue = if (isScrolled) 6.dp else 0.dp,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "fabElevation"
    )

    var isScheduleShowed by remember { mutableStateOf(true) }
    var isEventsShowed by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Column() {
                CalendarTopBar(
                    uiState = uiState,
                    calendarState = calendarState,
                    onCalendarStateChange = { calendarState = it },
                    isWeekSelectionVisible = isWeekSelectionVisible,
                    onWeekSelectionVisibilityChange = { isWeekSelectionVisible = it },
                    onEvent = onEvent,
                    modifier = Modifier
                        .background(topBarColor)
                        .statusBarsPadding()
                        .displayCutoutPadding()
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                )
                {
                    if (uiState.sessiaTimeSlots.isNotEmpty()){
                        item {
                            FilterChip(
                                onClick = {
                                    uiState.sessiaTimeSlots.firstOrNull()?.getDate()?.let { date ->
                                        calendarState = calendarState.copy(selectedDate = date)
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors().copy(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                                ),
                                label = {
                                    Text("Расписание сессии")
                                },
                                selected = true,
                            )
                        }
                    }

                    item {
                        FilterChip(
                            onClick = { isScheduleShowed = !isScheduleShowed },
                            label = {
                                Text("Расписание занятий")
                            },
                            selected = isScheduleShowed,
                        )
                    }

                    item {
                        FilterChip(
                            onClick = { isEventsShowed = !isEventsShowed },
                            label = {
                                Text("События")
                            },
                            selected = isEventsShowed,
                        )
                    }

                }

            }




        },
        floatingActionButton = {
            val today = remember { LocalDate.now() }
            AnimatedVisibility(
                visible = calendarState.selectedDate != today,
                enter = fadeIn(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                ),
                exit = fadeOut(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                )
                ) {
                ExtendedFloatingActionButton(
                    onClick = { calendarState = calendarState.copy(selectedDate = today) },
                    icon = { Icon(Icons.OpenInNew, null) },
                    text = { Text(stringResource(R.string.to_today)) },
                )
            }
        }
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = pagerAlpha.value },
        ) { page ->
            val date = LocalDate.ofEpochDay(page.toLong())

            ScheduleDayList(
                timeSlots = uiState.timeSlots.filter { it.dateString == date.toString() },
                sessiaTimeSlots = uiState.sessiaTimeSlots.filter { it.dateString == date.toString() },
                isGroupSelected = uiState.group != null,
                isError = uiState.isError,
                contentPadding = contentPadding,
                onStateCreated = { listStates[page] = it },
                onStateDisposed = { listStates.remove(page) },
                isScheduleShowed = isScheduleShowed,
                isEventsShowed = isEventsShowed,
                onEvent = onEvent,
                events = uiState.events.filter { it.getDate() == date }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPhonePreview() {
    AppTheme() {
        CalendarScreenPhone(uiState = UiState())
    }
}
