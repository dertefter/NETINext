package com.dertefter.money.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.money.R
import com.dertefter.money.presentation.component.MoneyItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoneyScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.years.size })
    val scope = rememberCoroutineScope()
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

    Scaffold(
        topBar = {
            Column {
                AppToolbar(
                    title = stringResource(id = R.string.money_title),
                    navigationIcon = {
                        AppNavigationIcon(onClick = { onEvent(Event.OnNavigateBack) })
                    },
                    scrollBehavior = scrollBehavior
                )
                if (uiState.years.isNotEmpty()) {
                    SecondaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        edgePadding = 16.dp,
                        containerColor = containerColor,
                        divider = {}
                    ) {
                        uiState.years.forEachIndexed { index, year ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                text = { Text(text = year) }
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
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = {
                val currentYear = uiState.years.getOrNull(pagerState.currentPage)
                if (currentYear != null) {
                    onEvent(Event.OnUpdateMoneyForYear(currentYear))
                } else {
                    onEvent(Event.OnUpdateYears)
                }
            },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = uiState.isLoading
                )
            }
        ) {
            if (uiState.years.isEmpty() && !uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = R.string.money_no_data))
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top
                ) { pageIndex ->
                    val year = uiState.years[pageIndex]
                    val moneyItems = uiState.moneyData[year] ?: emptyList()

                    LaunchedEffect(year) {
                        if (moneyItems.isEmpty()) {
                            onEvent(Event.OnUpdateMoneyForYear(year))
                        }
                    }

                    if (moneyItems.isEmpty() && !uiState.isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(id = R.string.money_no_data))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            contentPadding = PaddingValues(MaterialTheme.spacing.defaultScreenPadding)
                        ) {
                            items(moneyItems) { item ->
                                MoneyItem(
                                    title = item.title,
                                    value = item.text
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
private fun MoneyScreenPreview() {
    AppTheme {
        MoneyScreen(
            onEvent = {},
            uiState = UiState(
                years = listOf("2024", "2023"),
                moneyData = mapOf(
                    "2024" to listOf(
                        MoneyItemDto(title = "Стипендия", text = "10 000 руб."),
                        MoneyItemDto(title = "Материальная помощь", text = "5 000 руб.")
                    ),
                    "2023" to listOf(
                        MoneyItemDto(title = "Стипендия", text = "9 000 руб.")
                    )
                ),
                isLoading = false
            )
        )
    }
}
