package com.dertefter.calendar.presentation.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
){
    Text(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(MaterialTheme.spacing.extraLarge)
            .fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.titleMediumEmphasized,
        color = MaterialTheme.colorScheme.onTertiaryContainer

    )
}

@Preview(showBackground = true)
@Composable
private fun EventItemPreview() {
    AppTheme {
        EventItem(
            title = "Sample Event Title"
        )
    }
}

