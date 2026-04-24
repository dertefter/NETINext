package com.dertefter.design.components.loading

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RotatedLinearWavyProgressIndicator(
    modifier: Modifier = Modifier,
    progress: (() -> Float)? = null
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val indicatorLength = maxHeight
        val indicatorModifier = Modifier
            .requiredWidth(indicatorLength)
            .rotate(90f)

        if (progress != null) {
            LinearWavyProgressIndicator(
                modifier = indicatorModifier,
                progress = progress,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                waveSpeed = 12.dp,
                wavelength = 18.dp,
                stopSize = 0.dp
            )
        } else {
            LinearWavyProgressIndicator(
                modifier = indicatorModifier,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRotatedIndicator() {
    RotatedLinearWavyProgressIndicator(
        modifier = Modifier.height(300.dp).width(10.dp),
        progress = { 0.5f }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewIndeterminateRotatedIndicator() {
    RotatedLinearWavyProgressIndicator(
        modifier = Modifier.height(300.dp).width(50.dp),
        progress = null
    )
}