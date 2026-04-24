package com.dertefter.neticlient.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.WideNavigationRail
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.WideNavigationRailItem
import androidx.compose.material3.WideNavigationRailValue
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dertefter.design.theme.AppTheme
import com.dertefter.neticlient.navigation.AppNavigationItem
import com.dertefter.neticlient.navigation.authorizedNavigationItems

@Composable
fun DesktopNavigationRail(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<AppNavigationItem>
) {

    val state = rememberWideNavigationRailState( WideNavigationRailValue.Expanded)

    WideNavigationRail(
        colors = WideNavigationRailDefaults.colors().copy(containerColor = Color.Transparent),
        state = state
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.tab::class) } == true
            WideNavigationRailItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.tab) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon(), contentDescription = item.label) },
                label = { Text(item.label) },
                railExpanded = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DesktopNavigationRailPreview() {
    AppTheme {
        DesktopNavigationRail(
            navController = rememberNavController(),
            currentDestination = null,
            navigationItems = authorizedNavigationItems
        )
    }
}
