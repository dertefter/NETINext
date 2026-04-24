package com.dertefter.design.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.R
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    onRetry: () -> Unit = {}
) {

    val title = title ?: stringResource(id = R.string.update_failed)

    Row(
        modifier = modifier
            .clip(MaterialTheme.cornerShape(MaterialTheme.spacing.extraLarge))
            .background(MaterialTheme.colorScheme.errorContainer)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){

        Icon(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .clip(RoundedCornerShape(MaterialTheme.spacing.large))
                .clickable(
                    onClick = onRetry,
                )
                .size(52.dp)
                .background(MaterialTheme.colorScheme.error)
                .padding(8.dp),
            imageVector = Icons.Cached,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(id = R.string.retry)
        )

        Column(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.medium, end = MaterialTheme.spacing.extraLarge)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            message?.let{ message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

        }

    }

}

@Preview(locale = "en", showBackground = true)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorCardePreview() {
    AppTheme {
        ErrorCard(
            title = "Не удалосб загурзитб данные",
            message = "Ляля тополя",
            onRetry = {},
            modifier = Modifier.padding(16.dp)
        )
    }

}