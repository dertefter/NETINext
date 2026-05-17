package com.dertefter.control_weeks.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.control_weeks.R
import com.dertefter.control_weeks.presentation.component.ControlWeekItem
import com.dertefter.data.dto.control_weeks.ControlWeekDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ControlWeeksScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val appBarColors = TopAppBarDefaults.topAppBarColors()

    val containerColor by animateColorAsState(
        targetValue = if (scrollBehavior.state.contentOffset < 0f) {
            appBarColors.scrolledContainerColor
        } else {
            appBarColors.containerColor
        },
        label = "TabRowContainerColor"
    )

    val groupedResults = remember(uiState.controlWeeks) {
        uiState.controlWeeks?.groupBy { it.semester }?.toSortedMap(nullsLast()) ?: emptyMap()
    }
    val semesters = remember(groupedResults) {
        groupedResults.keys.toList()
    }

    val pagerState = rememberPagerState(pageCount = { semesters.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(containerColor)
            ) {
                AppToolbar(
                    title = stringResource(R.string.control_weeks_title),
                    navigationIcon = {
                        AppNavigationIcon(
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            },
                        )
                    },
                    actions = {
                        AppNavigationIcon(
                            icon = Icons.Share,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            onClick = {
                                onEvent(Event.OnShare)
                            },
                        )
                    },
                    scrollBehavior = scrollBehavior
                )



                if (uiState.error != null) {
                    ErrorCard(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                        onRetry = { onEvent(Event.OnUpdateSessiaResults) }
                    )
                }


                if (semesters.isNotEmpty()) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage.coerceIn(0, semesters.size - 1),
                        edgePadding = MaterialTheme.spacing.medium,
                        containerColor = Color.Transparent,
                        divider = {},
                    ) {
                        semesters.forEachIndexed { index, semester ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = {
                                    Text(
                                        text = stringResource(R.string.control_weeks_semester_format, semester),
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    ) { contentPadding ->

        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = { onEvent(Event.OnUpdateSessiaResults) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .padding(top = contentPadding.calculateTopPadding())
                        .align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = uiState.isLoading
                )
            }
        ) {
            when {

                uiState.controlWeeks == null && uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLoadingIndicator()
                    }
                }

                uiState.controlWeeks == null && uiState.error == null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.control_weeks_no_control_weeks_data),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                uiState.controlWeeks?.isEmpty() == true -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.control_weeks_no_control_weeks_data),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = contentPadding,
                        modifier = Modifier.fillMaxSize(),
                    ) { pageIndex ->
                        val semester = semesters.getOrNull(pageIndex) ?: return@HorizontalPager
                        val results = groupedResults[semester] ?: emptyList()

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            modifier = Modifier
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .fillMaxSize()
                                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                            contentPadding = PaddingValues(vertical = MaterialTheme.spacing.medium)
                        ) {

                            if (results.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.control_weeks_no_control_weeks_data),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            itemsIndexed(
                                items = results,
                                key = { _, item -> item.hashCode() }
                            ) { index, controlWeek ->
                                ControlWeekItem(
                                    title = controlWeek.title,
                                    value = controlWeek.value,
                                    week = controlWeek.week,
                                    isTop = index == 0,
                                    isBottom = index == results.lastIndex
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun ControlWeeksScreenPreview() {
    val sampleResults = listOf(
        ControlWeekDto("Математический анализ", "1", "5-ая", "1"),
        ControlWeekDto("Физика", "1", "2", "1"),
        ControlWeekDto("Информатика", "2", "9-ая", "2"),
        ControlWeekDto("Попа муровья", "1", "9-ая", "2")
    )
    AppTheme {
        ControlWeeksScreen(
            onEvent = {},
            uiState = UiState(
                controlWeeks = sampleResults,
                isLoading = false,
                error = null
            )
        )
    }
}
