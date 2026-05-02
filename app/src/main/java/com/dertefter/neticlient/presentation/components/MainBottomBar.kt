package com.dertefter.neticlient.presentation.components

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dertefter.neticlient.navigation.TabRouteItem

@Composable
fun MainBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<TabRouteItem>,
) {
    NavigationBar {
        navigationItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.tab::class) } == true
            AppNavigationItem(
                label = item.label,
                icon = item.icon(),
                isSelected = isSelected,
                onClick = {
                    navController.navigate(item.tab) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
