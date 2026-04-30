package com.dertefter.settings_theme.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing


@Composable
fun ShapeItem(
    modifier: Modifier = Modifier,
    isCut: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    text: String = ""
) {

    val cornerRadius =  MaterialTheme.rounding.medium

    val shape = if (isCut){
        CutCornerShape(cornerRadius)
    } else {
        RoundedCornerShape(cornerRadius)
    }

    val containerColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    )

    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    )

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(MaterialTheme.spacing.large),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyLargeEmphasized,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShapeItemPreview() {
    AppTheme {
        ShapeItem(
            modifier = Modifier
                .padding(16.dp),
            text = "Круго"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShapeItemPreview2() {
    AppTheme {
        ShapeItem(
            isCut = true,
            modifier = Modifier
                .padding(16.dp),
            text = "Круго"
        )
    }
}