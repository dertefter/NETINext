package com.dertefter.messages.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun MessagesInfoAlertCard(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {}
) {

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
                    text = "Это не почтовый ящик!",
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
                Text(
                    text = "Здесь вы можете ознакомиться с сообщениями от преподавателей и служб",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.End),
            colors = ButtonDefaults.textButtonColors().copy(
                containerColor =  MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Понятно")
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