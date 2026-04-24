package com.dertefter.neticlient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.dertefter.navigation.Routes

data class AppNavigationItem(
    val label: String,
    val startDestination: Routes,
    val tab: Routes,
    val icon: @Composable () -> ImageVector
)
