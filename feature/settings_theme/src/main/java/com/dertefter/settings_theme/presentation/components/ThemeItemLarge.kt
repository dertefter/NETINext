package com.dertefter.settings_theme.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.settings_theme.R

@Composable
fun ThemeItemLarge(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
){

    val containerColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    )

    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    )

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(horizontal = MaterialTheme.spacing.extraLarge)
            .height(64.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
    ){
        Icon(
            imageVector = Icons.Palette,
            contentDescription = null,
            tint = contentColor
        )
        Text(
            text = stringResource(R.string.settings_theme_default_color),
            color = contentColor,
            style = MaterialTheme.typography.bodyLargeEmphasized
        )
    }


}

@Preview(showBackground = true)
@Composable
fun ThemeItemPreview2() {
    AppTheme {
        ThemeItemLarge(
            modifier = Modifier.padding(16.dp)
        )
    }
}
