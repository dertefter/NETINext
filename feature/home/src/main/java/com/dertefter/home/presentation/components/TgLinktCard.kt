package com.dertefter.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TgLinktCard(
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val title = "Дневничок разработки NETI Next"
    val text = "Подпишитесь и следите за обновлениями!"


    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .clip(MaterialTheme.shapes.largeIncreased)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {

        Row(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.large)
                .padding(horizontal = MaterialTheme.spacing.large)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {

            Icon(
                imageVector = Icons.TG,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleMediumEmphasized
                )
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                text = "Не показывать больше",
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )
        }

    }

}

@Preview(showBackground = false)
@Composable
fun TgLinktCardPreview() {
    AppTheme {
        TgLinktCard()
    }
}