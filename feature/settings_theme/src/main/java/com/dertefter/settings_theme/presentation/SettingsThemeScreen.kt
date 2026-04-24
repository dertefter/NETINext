package com.dertefter.settings_theme.presentation

 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.Arrangement
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
 import androidx.compose.ui.tooling.preview.Preview
 import com.dertefter.design.components.appbar.AppToolbar
 import com.dertefter.design.components.buttons.AppNavigationIcon
 import com.dertefter.design.icons.Icons
 import com.dertefter.design.theme.AppTheme
 import com.dertefter.design.theme.rounding
 import com.dertefter.design.theme.spacing
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
                List(5) { i ->
                    Color.hsv(i * (360f / 5), 1f, 1f).toArgb().toLong() and 0xFFFFFFFFL
                }
            }

            Scaffold(
                topBar = {
                    AppToolbar(
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
                            text = "Цвет темы",
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
                        ThemeItemLarge(
                            isSelected = uiState.color == null,
                            onClick = {
                                onEvent(Event.OnSelectColor(null))
                            }
                        )
                    }

                    item {
                        LazyRow(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                            contentPadding = PaddingValues(MaterialTheme.spacing.large)
                        ) {
                            items(colors) { color ->
                                ThemeItem(
                                    modifier = Modifier,
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

                    item {
                        Text(
                            text = "Форма углов",
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
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .padding(MaterialTheme.spacing.large)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
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
                                text = "Закругление"
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
                                text = "Обрезка"
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
