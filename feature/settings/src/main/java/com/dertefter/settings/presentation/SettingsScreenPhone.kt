package com.dertefter.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing
import com.dertefter.navigation.Routes
import com.dertefter.settings.R
import com.dertefter.settings.presentation.components.SettingsListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreenPhone(
    onEvent: (Event) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()

    val topShape = MaterialTheme.cornerShape(
        topStart = MaterialTheme.rounding.largeIncreased,
        topEnd = MaterialTheme.rounding.largeIncreased,
    )

    val bottomShape = MaterialTheme.cornerShape(
        bottomStart = MaterialTheme.rounding.largeIncreased,
        bottomEnd = MaterialTheme.rounding.largeIncreased,
    )


    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_title), navigationIcon = {
                    AppNavigationIcon(
                        icon = Icons.ArrowBack, onClick = {
                            onEvent(
                                Event.OnNavigateBack
                            )
                        })
                }, scrollBehavior = scrollBehavior
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
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            item {}
            item {
                SettingsListItem(
                    icon = Icons.User,
                    title = stringResource(R.string.settings_item_account_title),
                    subtitle = stringResource(R.string.settings_item_account_subtitle),
                    onClick = {
                        onEvent(Event.OnNavigateTo(Routes.SettingsAccount))
                    },
                    modifier = Modifier.clip(topShape)
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Notifications,
                    title = stringResource(R.string.settings_item_notifications_title),
                    subtitle = stringResource(R.string.settings_item_notifications_subtitle),
                    onClick = {
                        onEvent(Event.OnNavigateTo(Routes.SettingsNotifications))
                    },
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Palette,
                    title = stringResource(R.string.settings_item_theme_title),
                    subtitle = stringResource(R.string.settings_item_theme_subtitle),
                    onClick = {
                        onEvent(Event.OnNavigateTo(Routes.SettingsTheme))
                    },
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Experiment,
                    title = stringResource(R.string.settings_item_labs_title),
                    onClick = {
                        onEvent(Event.OnNavigateTo(Routes.SettingsLabs))
                    },
                    subtitle = stringResource(R.string.settings_item_labs_subtitle)
                )
            }

            item {
                SettingsListItem(
                    icon = Icons.Info,
                    title = stringResource(R.string.settings_item_about_title),
                    subtitle = stringResource(R.string.settings_item_about_subtitle),
                    modifier = Modifier.clip(bottomShape)
                )
            }
            item {}

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPhonePreview() {
    AppTheme {
        SettingsScreenPhone(onEvent = {})
    }
}
