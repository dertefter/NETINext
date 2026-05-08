package com.dertefter.messages.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.messages.R
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagesInfoAlertCard(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {}
) {
    val descriptionText = stringResource(R.string.messages_info_alert_description)
    val linkText = stringResource(R.string.messages_info_alert_link)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.largeIncreased)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {

        Row(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.large)
                .padding(horizontal = MaterialTheme.spacing.large)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {

            Icon(
                imageVector = Icons.Error,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialShapes.Cookie7Sided.toShape())
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(MaterialTheme.spacing.large),
                tint = MaterialTheme.colorScheme.onSecondary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.messages_info_alert_title),
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
                Text(
                    text = buildAnnotatedString {
                        append(descriptionText)
                        withLink(LinkAnnotation.Url("https://id.nstu.ru/email")) {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(linkText)
                            }
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        TextButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.End),
        ) {
            Text(
                text = stringResource(R.string.messages_info_alert_dismiss),
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

    }

}

@Preview(showBackground = false)
@Composable
fun MessagesInfoAlertCardPreview() {
    AppTheme {
        MessagesInfoAlertCard()
    }
}