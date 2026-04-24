package com.dertefter.settings_account.presentation

 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.itemsIndexed
 import androidx.compose.foundation.lazy.rememberLazyListState
 import androidx.compose.material3.Button
 import androidx.compose.material3.ButtonDefaults
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.TopAppBarDefaults
 import androidx.compose.material3.rememberTopAppBarState
 import androidx.compose.runtime.Composable
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.input.nestedscroll.nestedScroll
 import androidx.compose.ui.res.stringResource
 import androidx.compose.ui.tooling.preview.Preview
 import com.dertefter.design.components.appbar.AppToolbar
 import com.dertefter.design.components.auth.AuthRequestCard
 import com.dertefter.design.components.auth.SavedAccountCard
 import com.dertefter.design.components.buttons.AppNavigationIcon
 import com.dertefter.design.icons.Icons
 import com.dertefter.design.theme.AppTheme
 import com.dertefter.design.theme.cornerShape
 import com.dertefter.design.theme.rounding
 import com.dertefter.design.theme.spacing
 import com.dertefter.settings_account.R
 import com.dertefter.settings_account.presentation.components.SourceCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsAccountScreen(
    uiState: UiState,
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()

    val topShape = MaterialTheme.cornerShape(
        topStart = MaterialTheme.rounding.large,
        topEnd = MaterialTheme.rounding.large,
    )

    val bottomShape = MaterialTheme.cornerShape(
        bottomStart = MaterialTheme.rounding.large,
        bottomEnd = MaterialTheme.rounding.large,
    )

    Scaffold(
        topBar = {
            AppToolbar(
                navigationIcon = {
                    if (!isPanel){
                        AppNavigationIcon(
                            icon = Icons.ArrowBack,
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            }
                        )
                    }

                }
            )
        },
    ) { contentPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            if (uiState.login == null){
                item {
                    AuthRequestCard(
                        onClick = {
                            onEvent(Event.OnNavigateToAuth)
                        }
                    )
                }

            }
            else {

                item {
                    Text(
                        text = stringResource(R.string.your_account),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLargeEmphasized,
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .padding(vertical = MaterialTheme.spacing.small)
                            .padding(horizontal = MaterialTheme.rounding.large)
                            .fillMaxWidth()
                    )
                }

                item {
                    Text(
                        text = uiState.login,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLargeEmphasized,
                        modifier = Modifier
                            .clip(MaterialTheme.cornerShape(MaterialTheme.rounding.medium))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(
                                vertical = MaterialTheme.spacing.large,
                                horizontal = MaterialTheme.spacing.extraLarge
                            )
                            .fillMaxWidth()
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        Button(
                            shape = MaterialTheme.shapes.medium,
                            onClick = {
                                onEvent(Event.OnLogOut)
                            },
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                        ) {
                            Text(stringResource(R.string.logout))
                        }
                    }


                }

                item {
                    Text(
                        text = stringResource(R.string.status),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLargeEmphasized,
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .padding(vertical = MaterialTheme.spacing.small)
                            .padding(horizontal = MaterialTheme.rounding.large)
                            .fillMaxWidth()
                        )
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                    )
                    {
                        SourceCard(
                            title = stringResource(R.string.ciu),
                            authStatus = uiState.ciuAuthStatus,
                            onRetry = {
                                onEvent(Event.OnRetryCiu)
                            }
                        )
                        SourceCard(
                            title = stringResource(R.string.yourNeti),
                            authStatus = uiState.yourNetiAuthStatus,
                            onRetry = {
                                onEvent(Event.OnRetryYourNeti)
                            }

                        )
                    }
                }
            }

            if (uiState.loginHistory.isNotEmpty()){
                item {
                    Text(
                        text = stringResource(R.string.saved_accounts),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLargeEmphasized,
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.small)
                            .padding(vertical = MaterialTheme.spacing.small)
                            .padding(horizontal = MaterialTheme.rounding.large)
                            .fillMaxWidth()
                    )
                }

                itemsIndexed(uiState.loginHistory){ index, login ->
                    SavedAccountCard(
                        login = login,
                        onClick = {
                            onEvent(Event.OnChangeAccount(login))
                        },
                        onDelete = {
                            onEvent(Event.OnRemoveAccountFromHistory(login))
                        },
                        modifier = Modifier.clip(
                            if (index == 0){
                                topShape
                            } else if (index == uiState.loginHistory.lastIndex){
                                bottomShape
                            } else {
                                MaterialTheme.shapes.small
                            }
                        )
                    )
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsAccountScreenPreview() {
    AppTheme (
        isCut = true,
    ) {
        SettingsAccountScreen(onEvent = {}, uiState = UiState(
            login = "dd",
            loginHistory = listOf("ddd", "ddd", "eee")
        ))
    }
}
