package com.dertefter.swap_lks.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.loading.AppLoadingIndicator
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing

@Composable
fun LksItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    isLoading: Boolean = false,
) {

    val containerColor by animateColorAsState(
        if (isSelected){
            MaterialTheme.colorScheme.tertiaryContainer
        } else if (isLoading) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    )

    val contentColor by animateColorAsState(
        if (isSelected){
            MaterialTheme.colorScheme.onTertiaryContainer
        } else if (isLoading) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                onClick = onClick
            )
            .background(containerColor)
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = contentColor,
                )
            }

        }
        AnimatedVisibility(
            visible = isLoading
        ) {
            AppLoadingIndicator(
                color = contentColor
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun RouteItemPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)

        ) {
            LksItem(
                title = "dddjldidhhdvh",
                subtitle = "djkd jkc dsjkdsj vdk",
                onClick = {},
                isSelected = true
            )
        }

    }
}
