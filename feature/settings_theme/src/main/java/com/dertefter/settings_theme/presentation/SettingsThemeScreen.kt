package com.dertefter.settings_theme.presentation

 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.Arrangement
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
 import androidx.compose.material3.ExperimentalMaterial3Api
 import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Scaffold
 import androidx.compose.material3.Text
 import androidx.compose.material3.TopAppBarDefaults
 import androidx.compose.material3.rememberTopAppBarState
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.key
 import androidx.compose.runtime.remember
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.graphics.toArgb
 import androidx.compose.ui.input.nestedscroll.nestedScroll
 import androidx.compose.ui.res.stringResource
 import androidx.compose.ui.tooling.preview.Preview
 import com.dertefter.design.components.appbar.AppToolbar
 import com.dertefter.design.components.buttons.AppNavigationIcon
 import com.dertefter.design.icons.Icons
 import com.dertefter.design.theme.AppTheme
 import com.dertefter.design.theme.rounding
 import com.dertefter.design.theme.spacing
 import com.dertefter.settings_theme.R
 import com.dertefter.settings_theme.presentation.components.ShapeItem
 import com.dertefter.settings_theme.presentation.components.ThemeItem
 import com.dertefter.settings_theme.presentation.components.ThemeItemLarge

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsThemeScreen(
    uiState: UiState,
    onEvent: (Event) -> Unit,
    isPanel: Boolean = false,
) {
    val isCut = uiState.isShapeCut ?: false
    key(isCut) {
        AppTheme(
            seedColor = uiState.color,
            isCut = isCut,
            isPanel = isPanel
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
                        Text(
                            text = stringResource(R.string.settings_theme_color_title),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLargeEmphasized,
                            modifier = Modifier
                                .padding(top = MaterialTheme.spacing.small)
                                .padding(vertical = MaterialTheme.spacing.small)
                                .padding(horizontal = MaterialTheme.rounding.large)
                                .fillMaxWidth()
                        )
                    }

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
                        }

                    }

                    item {
                        Text(
                            text = stringResource(R.string.settings_theme_shape_title),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLargeEmphasized,
                            modifier = Modifier
                                .padding(top = MaterialTheme.spacing.small)
                                .padding(vertical = MaterialTheme.spacing.small)
                                .padding(horizontal = MaterialTheme.rounding.large)
                                .fillMaxWidth()
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.largeIncreased)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(MaterialTheme.spacing.large)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                        ) {
                            ShapeItem(
                                modifier = Modifier.weight(1f),
                                isSelected = uiState.isShapeCut != true,
                                isCut = false,
                                onClick = {
                                    onEvent(
                                        Event.OnSetIsShapeCut(false)
                                    )
                                },
                                text = stringResource(R.string.settings_theme_shape_round)
                            )
                            ShapeItem(
                                modifier = Modifier.weight(1f),
                                isSelected = uiState.isShapeCut == true,
                                isCut = true,
                                onClick = {
                                    onEvent(
                                        Event.OnSetIsShapeCut(true)
                                    )
                                },
                                text = stringResource(R.string.settings_theme_shape_cut)
                            )
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
