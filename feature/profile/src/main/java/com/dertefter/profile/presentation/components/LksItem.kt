package com.dertefter.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@Composable
fun LksItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(
                onClick = onClick
            )
            .background(MaterialTheme.colorScheme.tertiaryContainer)
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
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }

        }

        Icon(
            imageVector = Icons.SwapVert,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
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
                onClick = {}
            )
        }

    }
}
