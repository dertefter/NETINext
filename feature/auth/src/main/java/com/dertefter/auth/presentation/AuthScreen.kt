package com.dertefter.auth.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.text_fields.TextFieldItem
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreen(onEvent: (Event) -> Unit, uiState: UiState) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppToolbar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    AppNavigationIcon(
                        icon = Icons.ArrowBack,
                        onClick = {  }
                    )
                },
                title = "Авторизация"
            )
        }
    )
    { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier
                    .padding(top = LocalWindowInfo.current.containerDpSize.height / 6)
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    TextFieldItem(
                        value = uiState.login,
                        onValueChange = { onEvent(Event.OnLoginChanged(it)) },
                        hint = "Логин",
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading
                    )

                    TextFieldItem(
                        value = uiState.password,
                        onValueChange = { onEvent(Event.OnPasswordChanged(it)) },
                        hint = "Пароль",
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        trailingIcon = if (uiState.isPasswordVisible) Icons.VisibilityOff else Icons.Visibility,
                        onTrailingIconClick = { onEvent(Event.OnTogglePasswordVisibility) },
                        enabled = !uiState.isLoading
                    )
                }


            }

            AnimatedVisibility(uiState.isError) {
                Text(
                    color = MaterialTheme.colorScheme.error,
                    text = "Не удалось войти в акккаунт",
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.rounding.large, vertical = MaterialTheme.spacing.small)
                        .fillMaxWidth()
                )
            }

            Button(
                onClick = { onEvent(Event.OnSubmit) },
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth(),
                enabled = uiState.login.isNotBlank() && uiState.password.isNotBlank(),
                shape = MaterialTheme.circleShape()
            ) {
                AnimatedContent(
                    targetState = uiState.isLoading,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
                ) { isLoading ->
                    if (isLoading) {
                        LoadingIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Войти")
                    }
                }

            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AppTheme(
        isCut = true
    ) {
        AuthScreen(
            onEvent = {},
            uiState = UiState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview2() {
    AppTheme {
        AuthScreen(
            onEvent = {},
            uiState = UiState(isLoading = true, login = "f", password = "f")
        )
    }
}

