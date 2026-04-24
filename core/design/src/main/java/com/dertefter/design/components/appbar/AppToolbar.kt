package com.dertefter.design.components.appbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.gigamole.composefadingedges.FadingEdgesGravity
import com.gigamole.composefadingedges.verticalFadingEdges

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    AppToolbar(
        modifier = modifier,
        titleContent = {
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                AnimatedContent(
                    targetState = title,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                    },
                    label = "TitleAnimation"
                ) { targetTitle ->
                    if (targetTitle != null) {
                        Text(
                            text = targetTitle,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2
                        )
                    }
                }
                AnimatedContent(
                    targetState = subtitle,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                    },
                    label = "SubtitleAnimation"
                ) { targetSubtitle ->
                    if (targetSubtitle != null) {
                        Text(
                            text = targetSubtitle,
                            style = MaterialTheme.typography.labelLargeEmphasized,
                            maxLines = 2
                        )
                    }
                }
            }


        },
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {

    val containerColor by animateColorAsState(
        targetValue = if (scrollBehavior != null && scrollBehavior.state.contentOffset < 0f) {
            colors.scrolledContainerColor
        } else {
            colors.containerColor
        },
        label = "AppToolbarContainerColorAlpha"
    )

    Surface(
        modifier = modifier
            .heightIn(min = 68.dp)
            .fillMaxWidth(),
        color = containerColor
    ) {
        Row(
            modifier = Modifier
                .verticalFadingEdges(FadingEdgesGravity.Start)
                .statusBarsPadding()
                .displayCutoutPadding()
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding, vertical = MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.navigationIconContentColor) {
                navigationIcon()
            }

            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                CompositionLocalProvider(LocalContentColor provides colors.titleContentColor) {
                    titleContent()
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppToolbarPreview1() {
    AppTheme {
        AppToolbar(
            title = "Заголовок",
            subtitle = "ddd",
            navigationIcon = {
                AppNavigationIcon(
                    onClick = {}
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppToolbarPreview2() {
    AppTheme {
        AppToolbar(
            title = "Заголовок"
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AppToolbarPreview3() {
    AppTheme {
        AppToolbar(
            title = "Заголовок",
            navigationIcon = {
                AppNavigationIcon(
                    onClick = {}
                )
            }
        )
    }
}