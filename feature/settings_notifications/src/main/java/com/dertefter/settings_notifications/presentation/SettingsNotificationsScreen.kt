package com.dertefter.settings_notifications.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.settings_notifications.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsLabsScreen(
    uiState: UiState,
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {

    val context = LocalContext.current
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    LifecycleResumeEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        onPauseOrDispose { }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()


    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_notifications_title),
                navigationIcon = {
                    if (!isPanel) {
                        AppNavigationIcon(
                            icon = Icons.ArrowBack, onClick = {
                                onEvent(Event.OnNavigateBack)
                            })
                    }

                })
        },
    ) { contentPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            item {
                Image(
                    painter = painterResource(R.drawable.notification_example),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.spacing.large)
                        .clip(MaterialTheme.shapes.largeIncreased)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .padding(top = MaterialTheme.spacing.extraLarge)
                        .size(300.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.spacing.defaultScreenPadding)
                        .clip(MaterialTheme.shapes.largeIncreased),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {

                    if (!hasNotificationPermission) {
                        Row(modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .clickable {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(MaterialTheme.spacing.extraLarge),
                            verticalAlignment = Alignment.CenterVertically) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    style = MaterialTheme.typography.bodyLargeEmphasized,
                                    text = stringResource(R.string.settings_notifications_permission_required),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }

                            Icon(
                                imageVector = Icons.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)
                            )
                        }
                    }


                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(MaterialTheme.spacing.extraLarge),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                        ) {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = stringResource(R.string.settings_notifications_classes_title)
                            )
                            Text(
                                style = MaterialTheme.typography.bodySmall,
                                text = stringResource(R.string.settings_notifications_classes_desc)
                            )
                        }

                        Switch(
                            checked = uiState.isNotificationEnabled && hasNotificationPermission,
                            onCheckedChange = {
                                onEvent(
                                    Event.OnChangeNotificationStatus(it)
                                )
                            },
                            enabled = hasNotificationPermission
                        )
                    }


                }
            }


        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
private fun SettingsAccountScreenPreview() {
    AppTheme {
        SettingsLabsScreen(
            onEvent = {}, uiState = UiState(true)
        )
    }
}
