package com.dertefter.doc_detail.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.customColors
import com.dertefter.design.theme.spacing

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    colorize: Boolean = false,
    onClick: () -> Unit = {},

    ){

    val containerColor by animateColorAsState(
        if (!colorize) {
            MaterialTheme.colorScheme.surfaceContainerHigh
        } else if (text.contains("готово", ignoreCase = true)) {
            MaterialTheme.customColors.success
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    )

    val textColor by animateColorAsState(
        if (!colorize) {
            MaterialTheme.colorScheme.onSurface
        } else if (text.contains("готово", ignoreCase = true)) {
            MaterialTheme.customColors.onSuccess
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        }
    )

    val iconColor by animateColorAsState(
        if (!colorize) {
            MaterialTheme.colorScheme.primary
        } else if (text.contains("готово", ignoreCase = true)) {
            MaterialTheme.customColors.onSuccess
        } else {
            MaterialTheme.colorScheme.secondary
        }
    )

    val fontWeight by animateIntAsState(
        if (!colorize) {
            450
        } else {
            800
        }
    )


    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding()
            .padding(
                vertical = MaterialTheme.spacing.extraLarge,
                horizontal = MaterialTheme.spacing.extraLarge
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
    )
    {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f),
            fontWeight = FontWeight(fontWeight),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageCardPreview() {
    AppTheme {
        DetailItem(
            text = "Готово",
            colorize = true,
            icon = Icons.UserFilled
        )
    }
}
