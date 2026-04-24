package com.dertefter.settings_notifications.presentation

 import android.Manifest
 import android.content.pm.PackageManager
 import android.content.res.Configuration
 import android.os.Build
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.contract.ActivityResultContracts
 import androidx.annotation.RequiresApi
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
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    LifecycleResumeEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
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
                navigationIcon = {
                    if (!isPanel){
                        AppNavigationIcon(
                            icon = Icons.ArrowBack,
                            onClick = {
                                onEvent(Event.OnNavigateBack)
                            }
                        )
                    }

                }
            )
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
        )
        {


            item {
                Image(
                    painter = painterResource(R.drawable.notification_example),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                        .size(300.dp)
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.spacing.defaultScreenPadding)
                        .clip(MaterialTheme.shapes.large),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {

                    if (!hasNotificationPermission) {
                        Row(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .clickable {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                }
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(MaterialTheme.spacing.large),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    style = MaterialTheme.typography.bodyLargeEmphasized,
                                    text = "Для работы уведомлений необходимо разрешение",
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
                            .padding(MaterialTheme.spacing.large),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Column(
                            modifier = Modifier.weight(1f)
                        ){
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = "Уведомления о занятиях"
                            )
                            Text(
                                style = MaterialTheme.typography.bodySmall,
                                text = "Показывать текущие и будущие занятия в уведомлениях"
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

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
private fun SettingsAccountScreenPreview() {
    AppTheme() {
        SettingsLabsScreen(onEvent = {},
            uiState = UiState(true)
        )
    }
}
