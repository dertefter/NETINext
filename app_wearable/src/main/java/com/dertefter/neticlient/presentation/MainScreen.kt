package com.dertefter.neticlient.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.dertefter.calendar.ScheduleRoute
import com.dertefter.home.HomeRoute
import com.dertefter.navigation_wearable.NavigationAction
import com.dertefter.navigation_wearable.Navigator
import com.dertefter.navigation_wearable.Routes
import com.dertefter.neticlient.presentation.theme.NETIClientTheme
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.nav.SwipeDismissableNavHost
import com.google.android.horologist.compose.nav.composable

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun MainScreen(
    navigator: Navigator
) {
    NETIClientTheme {
        AppScaffold {
            val navController = rememberSwipeDismissableNavController()

            LaunchedEffect(Unit) {
                navigator.navigationActions.collect { action ->
                    when (action) {
                        is NavigationAction.Navigate -> {
                            navController.navigate(action.route)
                        }

                        is NavigationAction.NavigateAndClearBackStack -> {
                            navController.navigate(action.route) {
                                popUpTo(action.popupTo) {
                                    inclusive = action.inclusive
                                }
                            }
                        }

                        NavigationAction.NavigateUp -> {
                            navController.navigateUp()
                        }

                    }
                }
            }

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = Routes.Home
            ) {
                composable<Routes.Home> {
                    HomeRoute()
                }
                composable<Routes.Calendar> {
                    ScheduleRoute()
                }
            }
        }
    }
}
