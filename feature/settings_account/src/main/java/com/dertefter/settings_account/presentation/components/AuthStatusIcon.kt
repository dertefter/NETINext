package com.dertefter.settings_account.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.data.dto.auth.AuthStatus
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthStatusIcon(
    modifier: Modifier = Modifier,
    authStatus: AuthStatus,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer
){
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ){
        AnimatedContent(authStatus) { authStatus ->
            when (authStatus){
                is AuthStatus.Authorized -> {
                    Icon(
                        imageVector = Icons.NetworkNode,
                        contentDescription = null,
                        tint = contentColor,
                    )
                }
                is AuthStatus.Error -> {
                    Icon(
                        imageVector = Icons.Error,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
                is AuthStatus.Loading -> {
                    AppLoadingIndicator(
                        color = contentColor
                    )
                }
                AuthStatus.Unauthorized -> {
                    AppLoadingIndicator(
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthStatusIconPreview() {
    AppTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AuthStatusIcon(authStatus = AuthStatus.Unauthorized)
            AuthStatusIcon(authStatus = AuthStatus.Loading(login = "test"))
            AuthStatusIcon(authStatus = AuthStatus.Authorized(login = "test"))
            AuthStatusIcon(authStatus = AuthStatus.Error(login = "test"))
        }
    }
}
