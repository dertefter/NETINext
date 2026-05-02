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
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.spacing
import com.dertefter.neticlient.navigation.AppNavHost
import com.dertefter.neticlient.navigation.TabRouteItem
import com.dertefter.neticlient.presentation.Event
import com.dertefter.neticlient.presentation.components.AuthNotifyCard
import com.dertefter.neticlient.presentation.components.MainNavigationRail
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabUI(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<TabRouteItem> = emptyList(),
    onEvent: (Event) -> Unit,
    authStatusCiu: AuthStatus,
    authStatusYourNeti: AuthStatus,
    navHost: @Composable (Modifier) -> Unit = { modifier ->
        AppNavHost(
            modifier = modifier,
            navController = navController,
            navigationItems
        )
    }
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

    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier
                .imePadding()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxSize())
            {
                MainNavigationRail(
                    navController = navController,
                    currentDestination = currentDestination,
                    navigationItems = navigationItems,
                )
                navHost(
                    Modifier
                        .consumeWindowInsets(
                            WindowInsets(
                                left = paddingValues.calculateLeftPadding(LocalLayoutDirection.current),
                            )
                        )
                        .weight(1f)
                        .fillMaxSize()
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