package com.dertefter.neticlient

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dertefter.navigation.Navigator
import com.dertefter.neticlient.foreground.ScheduleService
import com.dertefter.neticlient.presentation.MainScreen
import com.dertefter.neticlient.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.screenState.value == null
        }

        lifecycleScope.launch {
            viewModel.screenState
                .map { it?.isNotificationEnabled }
                .distinctUntilChanged()
                .collect { isEnabled ->
                    val intent = Intent(this@MainActivity, ScheduleService::class.java)
                    if (isEnabled == true) {
                        startForegroundService(intent)
                    } else {
                        stopService(intent)
                    }
                }
        }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        setContent {
            val screenState by viewModel.screenState.collectAsStateWithLifecycle()
            val themeState by viewModel.themeState.collectAsStateWithLifecycle()
            screenState?.let { screenState ->
                MainScreen(
                    navigator,
                    screenState = screenState,
                    themeState = themeState,
                    onEvent = { event ->
                        viewModel.onEvent(event)
                    },
                )
            }
        }
    }
}
