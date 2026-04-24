package com.dertefter.neticlient.presentation.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dertefter.neticlient.presentation.components.MainBottomBar
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun PhoneUi(
    navController: NavHostController,
    currentDestination: NavDestination?,
    navigationItems: List<AppNavigationItem> = emptyList(),
    onEvent: (Event) -> Unit,
    authStatusNotify: AuthStatus?,
){


    val hazeState = rememberHazeState()

    Scaffold(
        bottomBar = {
            MainBottomBar(
                navController = navController,
                currentDestination = currentDestination,
                navigationItems = navigationItems,
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(bottom =  paddingValues.calculateBottomPadding())
                .consumeWindowInsets(
                    WindowInsets(
                        bottom = paddingValues.calculateBottomPadding()
                    )
                )
                .imePadding()
                .fillMaxSize()
        ) {
            AppNavHost(
                modifier = Modifier
                    .hazeSource(state = hazeState),
                navController = navController,
                navigationItems
            )

            AnimatedContent(
                targetState = authStatusNotify,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()).togetherWith(
                        slideOutVertically { it } + fadeOut()
                    )
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                label = "auth_notify_anim"
            ) { status ->
                if (status != null) {
                    AuthNotifyCard(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium),
                        authStatus = status,
                        hazeState = hazeState,
                        onClick = {
                            if (status is AuthStatus.Error) {
                                onEvent(Event.OnRetryAuthorize)
                            }
                        }
                    )
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PhoneUiPreview() {
    AppTheme {
        PhoneUi(
            authStatusNotify = AuthStatus.Loading("djdskjdsjksd"),
            navController = rememberNavController(),
            currentDestination = null,
            onEvent = {}
        )
    }
}
