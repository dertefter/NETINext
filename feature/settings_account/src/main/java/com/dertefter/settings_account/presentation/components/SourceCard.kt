package com.dertefter.settings_account.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.settings_account.R
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing


@Composable
fun SourceCard(
    modifier: Modifier = Modifier,
    title: String,
    authStatus: AuthStatus,
    onRetry: () -> Unit = {}
){

    val cardColor = when (authStatus){
        is AuthStatus.Authorized -> MaterialTheme.colorScheme.primaryContainer
        is AuthStatus.Error -> MaterialTheme.colorScheme.errorContainer
        is AuthStatus.Loading -> MaterialTheme.colorScheme.surfaceContainer
        is AuthStatus.Unauthorized -> MaterialTheme.colorScheme.surfaceContainer
    }

    val textColor = when (authStatus){
        is AuthStatus.Authorized -> MaterialTheme.colorScheme.onPrimaryContainer
        is AuthStatus.Error -> MaterialTheme.colorScheme.onErrorContainer
        is AuthStatus.Loading -> MaterialTheme.colorScheme.onSurface
        is AuthStatus.Unauthorized -> MaterialTheme.colorScheme.onSurface
    }

    val iconBackgroundColor = when (authStatus){
        is AuthStatus.Authorized -> MaterialTheme.colorScheme.primary
        is AuthStatus.Error -> MaterialTheme.colorScheme.error
        is AuthStatus.Loading -> MaterialTheme.colorScheme.secondary
        is AuthStatus.Unauthorized -> MaterialTheme.colorScheme.surfaceVariant
    }

    val iconColor = when (authStatus){
        is AuthStatus.Authorized -> MaterialTheme.colorScheme.onPrimary
        is AuthStatus.Error -> MaterialTheme.colorScheme.onError
        is AuthStatus.Loading -> MaterialTheme.colorScheme.onSecondary
        is AuthStatus.Unauthorized -> MaterialTheme.colorScheme.onSurfaceVariant
    }



    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = {
                if (authStatus is AuthStatus.Error){
                    onRetry()
                }
            })
            .background(cardColor)
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth()
    ){
        AuthStatusIcon(
            authStatus = authStatus,
            contentColor = iconColor,
            containerColor = iconBackgroundColor
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLargeEmphasized,
                color = textColor
            )
            Text(
                text = when (authStatus){
                    is AuthStatus.Authorized -> stringResource(R.string.settings_account_auth_status_authorized)
                    is AuthStatus.Error -> stringResource(R.string.settings_account_auth_status_error)
                    is AuthStatus.Loading -> stringResource(R.string.settings_account_auth_status_loading)
                    AuthStatus.Unauthorized -> stringResource(R.string.settings_account_auth_status_unauthorized)
                },
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun SourceCardPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SourceCard(
                title = "Google Calendar",
                authStatus = AuthStatus.Authorized("user@gmail.com")
            )
            SourceCard(
                title = "Outlook Calendar",
                authStatus = AuthStatus.Unauthorized
            )
            SourceCard(
                title = "Ilya's Schedule",
                authStatus = AuthStatus.Loading("ilya")
            )
        }
    }
}
