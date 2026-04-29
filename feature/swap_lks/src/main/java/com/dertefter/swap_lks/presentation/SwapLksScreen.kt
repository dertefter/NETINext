package com.dertefter.swap_lks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.user.LksDto
import com.dertefter.design.components.appbar.Headline
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.swap_lks.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SwapLksScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Headline(
                text = stringResource(R.string.swap_lks_title),
                modifier = Modifier.padding(MaterialTheme.spacing.defaultScreenPadding)
                )
        },
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.defaultScreenPadding)
        ) {

            if (uiState.error != null){
                item {
                    ErrorCard(
                        onRetry = {
                            onEvent(
                                Event.OnUpdateLks
                            )
                        }
                    )
                }
            }

            if (uiState.isLoading && uiState.lksList.isNullOrEmpty()){
                item {
                    Box(
                        Modifier
                            .padding(MaterialTheme.spacing.extraLarge)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLoadingIndicator()
                    }
                }
            } else {

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.largeIncreased)
                            .fillMaxWidth()
                    ) {
                        for (item in uiState.lksList ?: emptyList()) {
                            LksItem(
                                title = item.title,
                                subtitle = item.subtitle,
                                isSelected = item.isSelected,
                                isLoading = item.id != null && item.id == uiState.settingLksId,
                                onClick = {
                                    item.id?.let { lksId ->
                                        onEvent(
                                            Event.OnSetLks(lksId)
                                        )
                                    }

                                }
                            )
                        }
                    }

                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(vertical = MaterialTheme.spacing.extraSmall)
                                .size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.info_lks),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwapLksScreenPreview() {
    val sampleLksList = listOf(
        LksDto(
            title = "Личный кабинет 1",
            subtitle = "Студент",
            id = 1,
            isSelected = true
        ),
        LksDto(
            title = "Личный кабинет 2",
            subtitle = "Студент",
            id = 2,
            isSelected = false
        )
    )

    AppTheme {
        SwapLksScreen(
            uiState = UiState(
                lksList = sampleLksList,
            ),
            onEvent = {},
        )
    }
}

