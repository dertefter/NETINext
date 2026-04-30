package com.dertefter.settings_account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.auth.AuthRequestCard
import com.dertefter.design.components.auth.SavedAccountCard
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
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

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_account_title),
                navigationIcon = {
                    if (!isPanel) {
                        AppNavigationIcon(
                            icon = Icons.ArrowBack, onClick = {
                                onEvent(Event.OnNavigateBack)
                            })
                    }

                })
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
        ) {

            if (uiState.login == null) {
                item {
                    AuthRequestCard(
                        onClick = {
                            onEvent(Event.OnNavigateToAuth)
                        })
                }

            } else {

                item {
                    Text(
                        text = stringResource(R.string.settings_account_logged_in_as),
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
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.largeIncreased)
                            .clickable(
                                onClick = {
                                    onEvent(
                                        Event.OnLogOut
                                    )
                                }
                            )
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(MaterialTheme.spacing.extraLarge)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                    ) {
                        Text(
                            text = uiState.login,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Text(
                            text = stringResource(R.string.settings_account_logout),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.bodyMediumEmphasized,
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.error)
                                .padding(MaterialTheme.spacing.medium)
                                .fillMaxWidth()
                        )
                    }

                }

                item {
                    Text(
                        text = stringResource(R.string.settings_account_connection_status),
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
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased)
                    ) {
                        SourceCard(
                            title = stringResource(R.string.settings_account_source_ciu),
                            authStatus = uiState.ciuAuthStatus,
                            onRetry = {
                                onEvent(Event.OnRetryCiu)
                            })
                        SourceCard(
                            title = stringResource(R.string.settings_account_source_yourneti),
                            authStatus = uiState.yourNetiAuthStatus,
                            onRetry = {
                                onEvent(Event.OnRetryYourNeti)
                            }

                        )
                    }
                }
            }

            if (uiState.loginHistory.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.settings_account_switch_account),
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
                        modifier = Modifier.clip(MaterialTheme.shapes.largeIncreased)
                    ){
                        for (login in uiState.loginHistory){
                            SavedAccountCard(
                                login = login,
                                onClick = {
                                    onEvent(Event.OnChangeAccount(login))
                                },
                                onDelete = {
                                    onEvent(Event.OnRemoveAccountFromHistory(login))
                                },
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
private fun SettingsAccountScreenPreview() {
    AppTheme(
        isCut = false,
    ) {
        SettingsAccountScreen(
            onEvent = {}, uiState = UiState(
                login = "dd", loginHistory = listOf("ddd", "ddd", "eee")
            )
        )
    }
}
