package com.dertefter.docs.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.docs.R
import com.dertefter.docs.presentation.component.DocsItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DocsScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        onEvent(Event.OnUpdate)
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.docs_title),
                navigationIcon = {
                    AppNavigationIcon(
                        onClick = {
                            onEvent(
                                Event.OnNavigateBack
                            )
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
               icon = {
                   Icon(
                        imageVector = Icons.EditNote,
                       contentDescription = null
                   )
               },
                text = {
                    Text(stringResource(R.string.docs_create_request))
                },
                onClick = {
                    onEvent(
                        Event.OnNavigateToNewDocument
                    )
                }
            )
        }
    ) { contentPadding ->

        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            state = pullToRefreshState,
            isRefreshing = uiState.isLoading,
            onRefresh = {
                onEvent(
                    Event.OnUpdate
                )
            },
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

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                    .fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                item {  }

                if (uiState.isError){
                    item {
                        ErrorCard(
                            onRetry = {onEvent(Event.OnUpdate)}
                        )
                    }
                }

                itemsIndexed(uiState.docsList) { index, item ->
                    DocsItem(
                        onClick = {
                            onEvent(Event.OnOpenDocDetail(item))
                        },
                        type = item.type,
                        status = item.status,
                        isFirst = index == 0,
                        isLast =  index == uiState.docsList.lastIndex
                    )
                }

                if (uiState.docsList.isEmpty()){
                    if (uiState.isLoading){
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = LocalWindowInfo.current.containerDpSize.height / 2.5f
                                    )
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){
                                AppLoadingIndicator()
                            }
                        }
                    } else if (!uiState.isError){
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = LocalWindowInfo.current.containerDpSize.height / 2.5f
                                    )
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    stringResource(R.string.docs_empty_list)
                                )
                            }
                        }
                    }
                }

                item {  }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DocsScreenPreview() {
    AppTheme {
        DocsScreen(
            onEvent = {},
            uiState = UiState(
                docsList = emptyList(),
                isLoading = false,
                isError = false
            )
        )
    }
}

