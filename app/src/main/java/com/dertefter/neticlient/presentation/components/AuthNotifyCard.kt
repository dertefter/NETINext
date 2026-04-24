package com.dertefter.neticlient.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.neticlient.R
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AuthNotifyCard(
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    authStatus: AuthStatus?,
    onClick: () -> Unit = {}
) {

    val containerColor by animateColorAsState(
        targetValue = when (authStatus) {
            is AuthStatus.Loading -> MaterialTheme.colorScheme.onSecondaryContainer
            is AuthStatus.Authorized -> MaterialTheme.colorScheme.onPrimaryContainer
            is AuthStatus.Error -> MaterialTheme.colorScheme.onErrorContainer
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = when (authStatus) {
            is AuthStatus.Loading -> MaterialTheme.colorScheme.secondaryContainer
            is AuthStatus.Authorized -> MaterialTheme.colorScheme.primaryContainer
            is AuthStatus.Error -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceContainer
        },
        label = "contentColor"
    )

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(enabled = authStatus is AuthStatus.Error, onClick = onClick)
            .hazeEffect(
                state = hazeState,
                style = HazeMaterials.thick(
                    containerColor = containerColor
                )
            )
            .animateContentSize()
            .padding(MaterialTheme.spacing.small)
            .height(48.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedContent(
            targetState = authStatus,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                        (slideOutVertically { height -> -height } + fadeOut())
            },
            label = "authStatusText",
            modifier = Modifier.weight(1f)
        )
        { targetAuthStatus ->
            val title = when (targetAuthStatus) {
                is AuthStatus.Loading -> stringResource(R.string.auth_loading)
                is AuthStatus.Authorized -> stringResource(R.string.auth_success)
                is AuthStatus.Error -> stringResource(R.string.auth_error)
                else -> null
            }

            val caption = when (targetAuthStatus) {
                is AuthStatus.Loading -> targetAuthStatus.login
                is AuthStatus.Authorized -> targetAuthStatus.login
                is AuthStatus.Error -> stringResource(R.string.auth_retry)
                else -> null
            }

            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraLarge)
            ) {
                title?.let { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor
                    )
                }

                caption?.let { caption ->
                    Text(
                        text = caption,
                        style = MaterialTheme.typography.labelMediumEmphasized,
                        color = contentColor
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = authStatus is AuthStatus.Loading,
        ) {
            LoadingIndicator(
                color = contentColor
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
fun AuthNotifyCardLoadingPreview() {
    AppTheme {
        AuthNotifyCard(
            modifier = Modifier.padding(24.dp),
            authStatus = AuthStatus.Loading("ivanov_ii"),
        )
    }
}

@Preview(showBackground = true, name = "Authorized")
@Composable
fun AuthNotifyCardAuthorizedPreview() {
    AppTheme {
        AuthNotifyCard(
            modifier = Modifier.padding(24.dp),
            authStatus = AuthStatus.Authorized("ivanov_ii"),
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
fun AuthNotifyCardErrorPreview() {
    AppTheme {
        AuthNotifyCard(
            modifier = Modifier.padding(24.dp),
            authStatus = AuthStatus.Error("ivanov_ii"),
        )
    }
}
