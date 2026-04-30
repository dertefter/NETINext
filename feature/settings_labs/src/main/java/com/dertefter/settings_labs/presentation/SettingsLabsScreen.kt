package com.dertefter.settings_labs.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
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
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.settings_labs.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsLabsScreen(
    uiState: UiState,
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()

    val autoLabel = stringResource(R.string.settings_labs_source_auto)
    val yourNetiLabel = stringResource(R.string.settings_labs_source_yourneti)
    val ciuLabel = stringResource(R.string.settings_labs_source_ciu)


    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_labs_title),
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

            item {
                Text(
                    text = stringResource(R.string.settings_labs_preferred_source_title),
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
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(
                            horizontal = MaterialTheme.spacing.extraLarge,
                            vertical = MaterialTheme.spacing.large
                        )
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {

                    ButtonGroup(
                        overflowIndicator = { menuState ->
                            ButtonGroupDefaults.OverflowIndicator(menuState = menuState)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                    ) {
                        toggleableItem(
                            onCheckedChange = {
                                onEvent(Event.OnSelectPreferredDataSource(PreferredRemoteSource.AUTO))
                            },
                            label = autoLabel,
                            weight = 1f,
                            checked = uiState.preferredDataSource == PreferredRemoteSource.AUTO
                        )
                        toggleableItem(
                            onCheckedChange = {
                                onEvent(Event.OnSelectPreferredDataSource(PreferredRemoteSource.YOURNETI))
                            },
                            label = yourNetiLabel,
                            weight = 1f,
                            checked = uiState.preferredDataSource == PreferredRemoteSource.YOURNETI
                        )
                        toggleableItem(
                            onCheckedChange = {
                                onEvent(Event.OnSelectPreferredDataSource(PreferredRemoteSource.CIU))
                            },
                            label = ciuLabel,
                            weight = 1f,
                            checked = uiState.preferredDataSource == PreferredRemoteSource.CIU
                        )
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
        isCut = true
    ) {
        SettingsLabsScreen(
            onEvent = {}, uiState = UiState(
                preferredDataSource = PreferredRemoteSource.CIU
            )
        )
    }
}
