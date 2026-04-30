package com.dertefter.share_score.presentation

import android.content.ClipData
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.appbar.Headline
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import com.dertefter.share_score.R
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ShareScoreScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val shareScoreLabel = stringResource(R.string.share_score__label)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Headline(
                text = stringResource(R.string.share_score_title),
                modifier = Modifier.padding(MaterialTheme.spacing.defaultScreenPadding)
            )
        }
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.spacing.medium)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .size(240.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(MaterialTheme.spacing.extraLarge),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = uiState.isLoading to uiState.shareScoreLink,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "QrCodeAnimation"
                    ) { (isLoading, link) ->
                        when {
                            isLoading -> {
                                AppLoadingIndicator(color = MaterialTheme.colorScheme.onTertiaryContainer)
                            }

                            !link.isNullOrEmpty() -> {
                                QrCodeView(
                                    modifier = Modifier.fillMaxSize(),
                                    data = link,
                                    colors = QrCodeColors(
                                        foreground = MaterialTheme.colorScheme.onTertiaryContainer,
                                        background = MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                    dotShape = DotShape.Circle
                                )
                            }

                            else -> {
                                IconButton(onClick = { onEvent(Event.OnUpdateLink) }) {
                                    Icon(
                                        imageVector = Icons.Cached,
                                        contentDescription = stringResource(R.string.share_score_update_link),
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    AnimatedVisibility(
                        visible = !uiState.shareScoreLink.isNullOrEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        SelectionContainer {
                            Text(
                                text = uiState.shareScoreLink ?: "",
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    .padding(MaterialTheme.spacing.large)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.defaultScreenPadding)
                    ) {
                        AnimatedVisibility(
                            visible = !uiState.shareScoreLink.isNullOrEmpty(),
                            modifier = Modifier.weight(1f),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            FilledTonalButton(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.circleShape(),
                                onClick = {
                                    coroutineScope.launch {
                                        clipboard.setClipEntry(
                                            ClipEntry(
                                                ClipData.newPlainText(
                                                    shareScoreLabel,
                                                    uiState.shareScoreLink ?: ""
                                                )
                                            )
                                        )
                                    }
                                }
                            ) {
                                Text(stringResource(R.string.share_score_copy))
                            }
                        }

                        AnimatedVisibility(
                            visible = !uiState.isLoading,
                            modifier = Modifier.weight(1f),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.circleShape(),
                                onClick = { onEvent(Event.OnRegenerateLink) }
                            ) {
                                Text(stringResource(R.string.share_score_new_link))
                            }
                        }
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
                        text = stringResource(R.string.share_score_info),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShareScoreScreenPreview() {
    AppTheme {
        ShareScoreScreen(
            onEvent = {},
            uiState = UiState(
                shareScoreLink = "https://example.com/share/score/12345"
            )
        )
    }
}
