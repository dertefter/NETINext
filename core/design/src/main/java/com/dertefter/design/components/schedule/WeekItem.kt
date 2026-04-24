package com.dertefter.design.components.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import com.dertefter.design.shape.MorphPolygonShape
import com.dertefter.design.theme.AppTheme


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WeekItem(
    modifier: Modifier = Modifier,
    weekNumber: Int,
    isSelected: Boolean = false,
    isCurrentWeek: Boolean = false,
    onClick: () -> Unit = {}
) {

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCurrentWeek -> MaterialTheme.colorScheme.tertiary
            isSelected -> MaterialTheme.colorScheme.tertiaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        }, label = "backgroundColor"
    )

    val morph = remember {
        if (isCurrentWeek) {
            Morph(MaterialShapes.Pill, MaterialShapes.Cookie4Sided)
        } else {
            Morph(MaterialShapes.Pill, MaterialShapes.Circle)
        }

    }

    val shapeProgress by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "shapeProgress"
    )

    val animatedShape = remember(shapeProgress) {
        MorphPolygonShape(morph, shapeProgress)
    }

    val textColor by animateColorAsState(
        targetValue = when {
            isCurrentWeek -> MaterialTheme.colorScheme.onTertiary
            isSelected -> MaterialTheme.colorScheme.onTertiaryContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }, label = "textColor"
    )


    val fontWeight by animateIntAsState(
        targetValue = if (isSelected) 800 else 400
    )


    Box(
        modifier = modifier
            .clip(animatedShape)
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .size(40.dp)
    ) {
        AnimatedContent(

            weekNumber,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                            (slideOutVertically { height -> -height } + fadeOut())
                } else {
                    (slideInVertically { height -> -height } + fadeIn()) togetherWith
                            (slideOutVertically { height -> height } + fadeOut())
                }
            },
            modifier = Modifier.align(Alignment.Center)
        ) { weekNumber ->
            Text(
                color = textColor,
                text = weekNumber.toString(),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(fontWeight)
            )
        }

    }


}

@Preview
@Composable
fun WeekItemPreview() {
    AppTheme() {
        WeekItem(weekNumber = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun WeekItemPreview2() {

    AppTheme() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            WeekItem(weekNumber = 1, isSelected = false)

            WeekItem(weekNumber = 1, isSelected = true)

            WeekItem(weekNumber = 1, isSelected = false, isCurrentWeek = true)

            WeekItem(weekNumber = 1, isSelected = false)

            WeekItem(weekNumber = 10, isSelected = false)

            WeekItem(weekNumber = 11, isSelected = false)
            GroupButton()
        }

    }

}
