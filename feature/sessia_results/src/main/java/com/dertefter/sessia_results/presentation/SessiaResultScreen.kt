package com.dertefter.sessia_results.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.spacing
import com.dertefter.sessia_results.R
import com.dertefter.sessia_results.presentation.component.SessiaResultItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SessiaResultScreen(
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

    val groupedResults = remember(uiState.sessiaResults) {
        uiState.sessiaResults?.groupBy { it.semester }?.toSortedMap() ?: emptyMap()
    }
    val semesters = remember(groupedResults) {
        groupedResults.keys.toList()
    }

    val pagerState = rememberPagerState(pageCount = { semesters.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(semesters.isNotEmpty()) {
        if (semesters.isNotEmpty()) {
            pagerState.scrollToPage(semesters.size - 1)
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(containerColor)
            ) {
                AppToolbar(
                    title = stringResource(R.string.sessia_results),
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
                        onRetry = {onEvent(Event.OnUpdateSessiaResults)}
                    )
                }


                if (semesters.isNotEmpty()) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
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
                                        text = stringResource(R.string.semester_format, semester),
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
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding()),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading && uiState.sessiaResults != null,
            onRefresh = { onEvent(Event.OnUpdateSessiaResults) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    state = pullToRefreshState,
                    isRefreshing = uiState.isLoading
                )
            }
        ) {
            when {
                uiState.sessiaResults == null && !uiState.isLoading && uiState.error == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_data),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                uiState.sessiaResults?.isEmpty() == true -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_results),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                    ) { pageIndex ->
                        val semester = semesters.getOrNull(pageIndex) ?: return@HorizontalPager
                        val results = groupedResults[semester] ?: emptyList()

                        LazyColumn(
                            contentPadding = PaddingValues(
                                bottom = contentPadding.calculateBottomPadding(),
                                top = MaterialTheme.spacing.medium
                            ),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            modifier = Modifier
                                .nestedScroll(scrollBehavior.nestedScrollConnection)
                                .fillMaxSize()
                                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                        ) {
                            if (results.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.no_results_in_semester),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            itemsIndexed(
                                items = results,
                                key = { _, item -> item.hashCode() }
                            ) { index, sessiaResult ->
                                SessiaResultItem(
                                    sessiaResult = sessiaResult,
                                    modifier = Modifier.animateItem(),
                                    isTop = index == 0,
                                    isBottom = index == results.lastIndex
                                )
                            }

                            item {
                                Spacer(
                                    modifier = Modifier
                                        .height(MaterialTheme.spacing.small)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
