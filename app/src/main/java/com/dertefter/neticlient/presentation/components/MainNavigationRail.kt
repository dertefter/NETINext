package com.dertefter.neticlient.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dertefter.design.theme.spacing
import com.dertefter.neticlient.navigation.AppNavigationItem

@Composable
fun MainNavigationRail(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<AppNavigationItem>
) {
    NavigationRail(
        containerColor = Color.Transparent,
        modifier = Modifier.padding(vertical = MaterialTheme.spacing.defaultScreenPadding)
    ) {
        navigationItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.tab::class) } == true
            NavigationRailItem(
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
            )
        }
    }
}
