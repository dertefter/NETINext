package com.dertefter.neticlient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dertefter.design.icons.Icons
import com.dertefter.navigation.Routes
import com.dertefter.neticlient.R

@Composable
fun getNavigationMenu(isAuthorized: Boolean): List<TabRouteItem> {
    return if (isAuthorized){
        listOf(
            TabRouteItem(
                label = stringResource(R.string.app_route_home),
                startDestination = Routes.Home,
                icon = { Icons.Home },
                tab = Routes.Tab1
            ),

            TabRouteItem(
                label = stringResource(R.string.app_route_calendar),
                startDestination = Routes.Calendar,
                icon = { Icons.Calendar },
                tab = Routes.Tab2

            ),

            TabRouteItem(
                label = stringResource(R.string.app_route_messages),
                startDestination = Routes.Messages,
                icon = { Icons.Mail },
                tab = Routes.Tab3

            ),

            TabRouteItem(
                label = stringResource(R.string.app_route_profile),
                startDestination = Routes.Profile,
                icon = { Icons.User },
                tab = Routes.Tab4

            ),
        )

    } else {
        listOf(
            TabRouteItem(
                label = stringResource(R.string.app_route_home),
                startDestination = Routes.Home,
                icon = { Icons.Home },
                tab = Routes.Tab1
            ),

            TabRouteItem(
                label = stringResource(R.string.app_route_calendar),
                startDestination = Routes.Calendar,
                icon = { Icons.Calendar },
                tab = Routes.Tab2

            ),

            TabRouteItem(
                label = stringResource(R.string.app_route_profile),
                startDestination = Routes.Profile,
                icon = { Icons.User },
                tab = Routes.Tab4

            ),
        )

    }
}
