package com.dertefter.neticlient.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.AppTheme
import com.dertefter.neticlient.navigation.AppNavHost
import com.dertefter.neticlient.navigation.AppNavigationItem
import com.dertefter.neticlient.presentation.Event
import com.dertefter.neticlient.presentation.components.MainNavigationRail

@Composable
fun TabUI(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<AppNavigationItem> = emptyList(),
    onEvent: (Event) -> Unit,
    authStatusNotify: AuthStatus?,
){

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) { paddingValues ->
        Row(modifier = Modifier
            .imePadding()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .fillMaxSize())
        {
            MainNavigationRail(
                navController = navController,
                currentDestination = currentDestination,
                navigationItems = navigationItems,
            )
            AppNavHost(
                modifier = Modifier
                    .consumeWindowInsets(
                        WindowInsets(
                            left = paddingValues.calculateLeftPadding(LocalLayoutDirection.current),
                        )
                    )
                    .weight(1f)
                    .fillMaxSize(),
                navController = navController,
                navigationItems
            )
        }
    }

}

@Preview(showBackground = true, device = "spec:width=673dp,height=841dp,orientation=landscape",
    showSystemUi = true
)
@Composable
fun TabUIPreview() {
    AppTheme {
        TabUI(
            navController = rememberNavController(),
            currentDestination = null,
            onEvent = {},
            authStatusNotify = AuthStatus.Loading("djdskjdsjksd")
        )
    }
}
