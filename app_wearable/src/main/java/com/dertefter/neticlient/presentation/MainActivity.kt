package com.dertefter.neticlient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dertefter.neticlient.screens.home.HomeRoute
import com.dertefter.neticlient.presentation.theme.NETIClientTheme
import com.dertefter.neticlient.screens.schedule.ScheduleRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NETIClientTheme {
                ScheduleRoute()
            }
        }
    }
}