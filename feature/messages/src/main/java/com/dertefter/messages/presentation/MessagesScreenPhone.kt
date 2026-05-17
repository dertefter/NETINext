package com.dertefter.messages.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.messages.R
import com.dertefter.messages.presentation.components.FilterModeChip
import com.dertefter.messages.presentation.components.MessageItem
import com.dertefter.messages.presentation.components.MessagesInfoAlertCard
import com.dertefter.messages.presentation.components.SwipeAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagesScreenPhone(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()



    LaunchedEffect(uiState.filterMode) {
        scrollBehavior.state.contentOffset = 0f
    }


    Scaffold(
        topBar = {

            AppToolbar(
                scrollBehavior = scrollBehavior,
                title = if (uiState.filterMode is FilterMode.DELETED) {
                    stringResource(R.string.messages_archive)
                } else {
                    stringResource(R.string.messages_title)
                },
                actions = {
                    AppNavigationIcon(
                        icon = if (uiState.filterMode is FilterMode.DELETED) {
                            Icons.Close
                        } else {
                            Icons.Archive
                        }, onClick = {
                            if (uiState.filterMode is FilterMode.DELETED) {
                                onEvent(
                                    Event.OnUpdateFilterMode(FilterMode.ALL)
                                )
                            } else {
                                onEvent(
                                    Event.OnUpdateFilterMode(FilterMode.DELETED)
                                )
                            }

                        })
                })

        },
    ) { contentPadding ->

        val pullToRefreshState = rememberPullToRefreshState()

        if (uiState.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    AppLoadingIndicator()
                } else if (uiState.error != null) {
                    ErrorLarge(
                        onRetry = { onEvent(Event.OnUpdateMessages) }
                    )
                } else {
                    Text(
                        stringResource(R.string.messages_empty),
                        textAlign = TextAlign.Center,
                    )
                }
            }

        }
        PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = uiState.isLoading && scrollBehavior.state.contentOffset == 0f,
        onRefresh = { onEvent(Event.OnUpdateMessages) },
        indicator = {
            PullToRefreshIndicator(
                modifier = Modifier
                    .padding(contentPadding)
                    .align(Alignment.TopCenter),
                state = pullToRefreshState,
                isRefreshing = uiState.isLoading && scrollBehavior.state.contentOffset == 0f
            )
        })
        {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {

                if (!uiState.isAlertSkipped){
                    item{
                        MessagesInfoAlertCard(
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                            onClose = { onEvent(Event.OnSkipAlert) }
                        )
                    }
                }

                if (uiState.filterMode != FilterMode.DELETED) {
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(
                                horizontal = MaterialTheme.spacing.defaultScreenPadding + MaterialTheme.rounding.largeIncreased
                            ),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        ) {
                            items(uiState.filterModes, key = { it.hashCode() }) { mode ->
                                FilterModeChip(
                                    filterMode = mode,
                                    selected = uiState.filterMode == mode,
                                    onClick = { onEvent(Event.OnUpdateFilterMode(mode)) },
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }
                    }

                }

                if (uiState.error != null){
                    item {
                        ErrorCard (
                            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                            onRetry = { onEvent(Event.OnUpdateMessages) }
                        )
                    }
                }

                itemsIndexed(
                    uiState.messages, key = { index, message ->
                        val prev = uiState.messages.getOrNull(index - 1)?.id
                        val next = uiState.messages.getOrNull(index + 1)?.id
                        listOf(message.id, prev, next)
                    })
                { index, message ->

                    val prev = uiState.messages.getOrNull(index - 1)?.id
                    val next = uiState.messages.getOrNull(index + 1)?.id

                    MessageItem(
                        name = message.fioAuthor,
                        message = message.title,
                        avatarUrl = message.portraitUrl,
                        date = message.getLocalDateTime(),
                        isRead = message.isRead == 1,
                        onClick = {
                            onEvent(
                                Event.OnOpenMessageDetail(
                                    idStudent = message.idStudent, idMessage = message.id
                                )
                            )
                        },
                        onDelete = {
                            onEvent(
                                Event.OnDeleteForever(message.idStudent, message.id)
                            )
                        },
                        onArchive = {
                            onEvent(
                                Event.OnMoveToArchive(message.idStudent, message.id)
                            )
                        },
                        onRestore = {
                            onEvent(
                                Event.OnRemoveFromArchive(message.idStudent, message.id)
                            )
                        },
                        leftAction = if (message.isDeleted == 1) {
                            SwipeAction.DELETE
                        } else {
                            SwipeAction.ARCHIVE
                        },
                        rightAction = if (message.isDeleted == 1) {
                            SwipeAction.RESTORE
                        } else {
                            SwipeAction.ARCHIVE
                        },
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                            .animateItem(),
                        messageId = message.id,
                        prev = prev,
                        next = next
                    )
                }

                item{

                }

            }
        }



    }
}

@Preview(showBackground = true, apiLevel = 36)
@Composable
private fun MessagesScreenPhonePreview() {
    AppTheme {
        MessagesScreenPhone(
            onEvent = {},
            uiState = UiState(
                messages = listOf(
                    MessageDto(
                        dateSent = "2023-10-27T10:00:00",
                        fioAuthor = "Иванов Иван Иванович",
                        id = 1,
                        isDeleted = 0,
                        isRead = 0,
                        text = "Текст сообщения 1",
                        title = "Заголовок сообщения 1"
                    ),
                    MessageDto(
                        dateSent = "2023-10-27T09:00:00",
                        fioAuthor = "Петров Петр Петрович",
                        id = 2,
                        isDeleted = 0,
                        isRead = 1,
                        text = "Текст сообщения 2",
                        title = "Заголовок сообщения 2"
                    )
                ),
                filterModes = listOf(FilterMode.ALL, FilterMode.UNREAD, FilterMode.DELETED),
                filterMode = FilterMode.ALL
            )
        )
    }
}
