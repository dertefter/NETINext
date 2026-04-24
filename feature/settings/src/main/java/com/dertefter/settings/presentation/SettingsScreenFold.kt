package com.dertefter.settings.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dertefter.design.components.adaptive.PanelsLayout
import com.dertefter.navigation.Routes
import com.dertefter.settings_account.SettingsAccountRoute
import com.dertefter.settings_labs.SettingsLabsRoute
import com.dertefter.settings_notifications.SettingsNotificationsRoute
import com.dertefter.settings_theme.SettingsThemeRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreenFold(
    onEvent: (Event) -> Unit,
) {
    var currentRoute by remember { mutableStateOf<Routes?>(null) }

    PanelsLayout(
        modifier = Modifier.fillMaxSize(),
        contentLeft = {
            SettingsScreenPhone(
                onEvent = { event ->
                    if (event is Event.OnNavigateTo) {
                        currentRoute = event.route
                    } else {
                        onEvent(event)
                    }
                }
            )
        },
        contentRight = {
            when (currentRoute) {
                is Routes.SettingsAccount -> {
                    SettingsAccountRoute(isPanel = true)
                }
                is Routes.SettingsLabs -> {
                    SettingsLabsRoute(isPanel = true)
                }
                is Routes.SettingsTheme -> {
                    SettingsThemeRoute(isPanel = true)
                }
                is Routes.SettingsNotifications -> {
                    SettingsNotificationsRoute(isPanel = true)
                }
                else -> {

                }
            }
        }
    )
}
