package com.dertefter.design.components.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun SavedAccountCard(
    modifier: Modifier = Modifier,
    login: String,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {}
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ){
        Text(
            text = login,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onClick)
                .padding(MaterialTheme.spacing.medium)
        )
        AppNavigationIcon(
            onClick = onDelete,
            icon = Icons.Close,
            contentDescription = null,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

}

@Preview
@Composable
fun SavedAccountCardPreview(){
    AppTheme() {
        SavedAccountCard(login = "dd")

    }
}