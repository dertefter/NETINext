package com.dertefter.settings_theme.presentation

 import androidx.compose.foundation.background
 import androidx.compose.foundation.clickable
 import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.PaddingValues
 import androidx.compose.foundation.layout.Row
 import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.padding
 import androidx.compose.foundation.lazy.LazyColumn
 import androidx.compose.foundation.lazy.LazyRow
 import androidx.compose.foundation.lazy.items
 import androidx.compose.foundation.lazy.rememberLazyListState
 import androidx.compose.material3.DropdownMenu
 import androidx.compose.material3.DropdownMenuItem
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.graphics.toArgb
 import androidx.compose.ui.input.nestedscroll.nestedScroll
 import androidx.compose.ui.res.stringResource
 import androidx.compose.ui.tooling.preview.Preview
 import com.dertefter.data.dto.settings.ThemeStyle
 import com.dertefter.design.components.appbar.AppToolbar
 import com.dertefter.design.components.buttons.AppNavigationIcon
 import com.dertefter.design.icons.Icons
 import com.dertefter.design.theme.AppTheme
 import com.dertefter.design.theme.spacing
 import com.dertefter.settings_theme.R
 import com.dertefter.settings_theme.presentation.components.ThemeItem
 import com.dertefter.settings_theme.presentation.components.ThemeItemLarge

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsThemeScreen(
    uiState: UiState,
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()

    val colors: List<Long> = remember {
        List(12) { i ->
            Color.hsv(i * (360f / 12), 1f, 1f).toArgb().toLong() and 0xFFFFFFFFL
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.settings_theme_title),
                navigationIcon = {
                    if (!isPanel) {
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
    )
    { contentPadding ->

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
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.largeIncreased)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    ThemeItemLarge(
                        isSelected = uiState.color == null,
                        onClick = {
                            onEvent(Event.OnSelectColor(null))
                        }
                    )
                    LazyRow(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                        contentPadding = PaddingValues(MaterialTheme.spacing.large)
                    ) {
                        items(colors) { color ->
                            ThemeItem(
                                color = color,
                                onClick = {
                                    onEvent(
                                        Event.OnSelectColor(color)
                                    )
                                },
                                isSelected = color == uiState.color
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(
                                horizontal = MaterialTheme.spacing.extraLarge,
                                vertical = MaterialTheme.spacing.large
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Обрезать углы",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLargeEmphasized
                        )
                        Switch(
                            checked = uiState.isShapeCut == true,
                            onCheckedChange = {
                                onEvent(Event.OnSetIsShapeCut(it))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(
                                horizontal = MaterialTheme.spacing.extraLarge,
                                vertical = MaterialTheme.spacing.large
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Использовать старую палитру",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLargeEmphasized
                        )
                        Switch(
                            checked = uiState.newColorSpecVersion == true,
                            onCheckedChange = {
                                onEvent(Event.OnSetNewColorSpecVersion(it))
                            }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(
                                horizontal = MaterialTheme.spacing.extraLarge,
                                vertical = MaterialTheme.spacing.large
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Стиль цветовой палитры",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLargeEmphasized
                        )

                        var showStyleMenu by remember { mutableStateOf(false) }

                        Box {
                            Text(
                                text = uiState.themeStyle?.name ?: ThemeStyle.Vibrant.name,
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.small)
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickable(
                                        onClick = {
                                            showStyleMenu = true
                                        }
                                    )
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(
                                        vertical = MaterialTheme.spacing.medium,
                                        horizontal = MaterialTheme.spacing.large
                                    ),
                                color = MaterialTheme.colorScheme.onTertiary,
                                style = MaterialTheme.typography.bodyMediumEmphasized

                            )
                            DropdownMenu(
                                expanded = showStyleMenu,
                                onDismissRequest = { showStyleMenu = false }
                            ) {
                                ThemeStyle.entries.forEach { style ->
                                    DropdownMenuItem(
                                        text = { Text(style.name) },
                                        onClick = {
                                            onEvent(Event.OnSetThemeStyle(style))
                                            showStyleMenu = false
                                        }
                                    )
                                }
                            }
                        }

                    }


                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsAccountScreenPreview() {
    AppTheme {
        SettingsThemeScreen(onEvent = {},
            uiState = UiState(
                Color.Green.value.toLong()
            )
        )
    }
}
