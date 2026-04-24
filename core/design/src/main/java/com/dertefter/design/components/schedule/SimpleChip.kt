package com.dertefter.design.components.schedule

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing


enum class SimpleShipType {
    PRIMARY, SECONDARY, TERTIARY, DEFAULT, AUTO
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SimpleShip(
    modifier: Modifier = Modifier,
    label: String,
    type: SimpleShipType = SimpleShipType.AUTO,
    isHighlight: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge
){

    val type = if (type == SimpleShipType.AUTO){
        if (label.contains("практика", ignoreCase = true)){
            SimpleShipType.PRIMARY
        } else if ((label.contains("лекция", ignoreCase = true))) {
            SimpleShipType.SECONDARY
        } else if ((label.contains("лабо", ignoreCase = true))) {
            SimpleShipType.TERTIARY
        } else {
            SimpleShipType.DEFAULT
        }
    } else {
        type
    }


    val containerColor by animateColorAsState(
        targetValue = if (isHighlight) {
            MaterialTheme.colorScheme.primary
        } else {
            when (type) {
                SimpleShipType.PRIMARY -> MaterialTheme.colorScheme.primaryContainer
                SimpleShipType.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
                SimpleShipType.TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        },
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isHighlight) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            when (type) {
                SimpleShipType.PRIMARY -> MaterialTheme.colorScheme.onPrimaryContainer
                SimpleShipType.SECONDARY -> MaterialTheme.colorScheme.onSecondaryContainer
                SimpleShipType.TERTIARY -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        },
        label = "contentColor"
    )

    Text(
        text = label,
        style = textStyle,
        color = contentColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(MaterialTheme.circleShape())
            .background(containerColor)
            .padding(vertical = MaterialTheme.spacing.small, horizontal = MaterialTheme.spacing.large)
    )

}

@Preview
@Composable
fun SimpleChipPreview(){
    AppTheme(isCut = true) {
        SimpleShip(label ="sdlijdjidv")
    }
}
