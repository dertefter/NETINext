package com.dertefter.design.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
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
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorLarge(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    retryText: String? = null,
    onRetry: () -> Unit = {}
) {

    val title = title ?: stringResource(R.string.update_failed)
    val retryText = retryText ?: stringResource(R.string.retry)


    Column(
        modifier = modifier
            .padding(16.dp)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.HeartBrokenFilled,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.small)
                .clip(
                    shape = MaterialTheme.circleShape()
                )
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(12.dp)
                .size(48.dp)

        )

        Text(
            text = title,
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)
        )

        Button(
            onClick = onRetry,
            shape = MaterialTheme.circleShape()
        ) {
            Text(text = retryText)
        }

    }

}

@Preview(locale = "en", showBackground = true)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ErrorLargePreview() {
    AppTheme {
        ErrorLarge(
            onRetry = {}
        )
    }

}