package com.dertefter.messages_detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.messages_detail.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagesDetailScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
    isPanel: Boolean = false,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var mExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppToolbar(
                navigationIcon = {
                    if (!isPanel){
                        AppNavigationIcon(
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            },
                        )
                    }

                },
                actions = {
                    if (uiState.message != null){
                        Box {
                            AppNavigationIcon(
                                icon = Icons.MoreVert,
                                onClick = { mExpanded = true },
                            )
                            DropdownMenu(
                                expanded = mExpanded,
                                onDismissRequest = { mExpanded = false },
                                shape = MaterialTheme.shapes.medium
                            ) {
                                if (uiState.message.isDeleted == 1){
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.restore)) },
                                        onClick = {
                                            onEvent(Event.OnMoveToArchive(false))
                                            mExpanded = false
                                        }
                                    )
                                } else if (uiState.message.isDeleted == 0){
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.move_to_archive)) },
                                        onClick = {
                                            onEvent(Event.OnMoveToArchive(true))
                                            mExpanded = false
                                        }
                                    )
                                }
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.delete)) },
                                    onClick = {
                                        onEvent(Event.OnDelete)
                                        mExpanded = false
                                    }
                                )
                            }
                        }
                    }

                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (uiState.isLoading && uiState.message == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingIndicator()
                }
            }
            if (uiState.isError && uiState.message == null) {
                ErrorLarge(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { onEvent(Event.OnUpdateMessage) }
                )
            }
            SelectionContainer {
                LazyColumn(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize(),
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    if (uiState.message != null) {
                        item {
                            Text(
                                text = uiState.message.title,
                                modifier = Modifier
                                    .widthIn(max = 480.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                                style = MaterialTheme.typography.displaySmallEmphasized,
                            )
                        }

                        item {
                            Text(
                                modifier = Modifier
                                    .widthIn(max = 480.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                                    .padding(
                                        top = MaterialTheme.spacing.medium,
                                        bottom = MaterialTheme.spacing.defaultScreenPadding
                                    ),
                                text = AnnotatedString.fromHtml(htmlString = uiState.message.text),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }


                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessagesDetailScreenPreview() {
    AppTheme {
        MessagesDetailScreen(
            onEvent = {},
            uiState = UiState(
                message = MessageDto(
                    dateRead = null,
                    dateSent = "2023-08-23T09:24:58",
                    fioAuthor = "Иванов Иван Иванович",
                    id = 1,
                    idCategory = 1,
                    idAuthor = 123,
                    idStudent = 456,
                    isDeleted = 0,
                    isRead = 1,
                    messageUrl = null,
                    portraitUrl = null,
                    senderType = 1,
                    sname = "Иванов И.И.",
                    text = "Это текст тестового сообщения. Он может содержать <b>HTML</b> теги и быть достаточно длинным, чтобы проверить прокрутку и отображение контента на экране деталей.",
                    title = "Заголовок тестового сообщения",
                    withBlock = 0,
                    withPopup = 0
                ),
                isLoading = false,
                isError = true
            ),
        )
    }
}


