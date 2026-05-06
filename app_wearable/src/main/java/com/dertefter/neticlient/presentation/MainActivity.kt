package com.dertefter.neticlient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.dertefter.neticlient.presentation.theme.NETIClientTheme
import com.dertefter.neticlient.screens.home.HomeRoute
import com.dertefter.neticlient.screens.schedule.ScheduleRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NETIClientTheme {
                AppScaffold {
                    val navController = rememberSwipeDismissableNavController()
                    SwipeDismissableNavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeRoute(
                                onNavigateToSchedule = {
                                    navController.navigate("schedule")
                                }
                            )
                        }
                        composable("schedule") {
                            ScheduleRoute()
                        }
                    }
                }

            }
        }
    }
}
