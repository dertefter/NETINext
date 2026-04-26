package com.dertefter.neticlient.presentation.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.neticlient.navigation.AppNavHost
import com.dertefter.neticlient.navigation.AppNavigationItem
import com.dertefter.neticlient.presentation.Event
import com.dertefter.neticlient.presentation.components.AuthNotifyCard
import com.dertefter.neticlient.presentation.components.MainNavigationRail
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabUI(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<AppNavigationItem> = emptyList(),
    onEvent: (Event) -> Unit,
    authStatusCiu: AuthStatus,
    authStatusYourNeti: AuthStatus
){

    var isAuthNotifyVisible by rememberSaveable(authStatusCiu.toString(), authStatusYourNeti.toString()) {
        mutableStateOf(authStatusCiu !is AuthStatus.Unauthorized)
    }

    LaunchedEffect(authStatusCiu, authStatusYourNeti) {
        if (authStatusCiu !is AuthStatus.Unauthorized && isAuthNotifyVisible) {
            delay(4000)
            isAuthNotifyVisible = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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

            AnimatedVisibility (
                visible = isAuthNotifyVisible,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.spacing.medium),
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                val dismissState = rememberSwipeToDismissBoxState()
                LaunchedEffect(dismissState.currentValue) {
                    if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
                        isAuthNotifyVisible = false
                    }
                }
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {}
                ) {
                    AuthNotifyCard(
                        ciuAuthStatus = authStatusCiu,
                        yourNetiAuthStatus = authStatusYourNeti,
                        onRetry = {
                            onEvent(Event.OnRetryAuthorizeCiu)
                        },
                        onRetryYourNeti = {
                            onEvent(Event.OnRetryAuthorizeYourNeti)
                        }
                    )
                }
            }
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
            authStatusCiu = AuthStatus.Loading("djdskjdsjksd"),
            authStatusYourNeti = AuthStatus.Unauthorized
        )
    }
}
