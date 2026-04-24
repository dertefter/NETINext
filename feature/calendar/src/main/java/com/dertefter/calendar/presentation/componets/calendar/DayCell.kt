package com.dertefter.calendar.presentation.componets.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import com.dertefter.design.shape.MorphPolygonShape
import java.time.LocalDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    onDateClick: (LocalDate) -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isToday -> MaterialTheme.colorScheme.primary
            isSelected -> MaterialTheme.colorScheme.secondaryContainer
            else -> Color.Transparent
        }, label = "backgroundColor"
    )

    val morph = remember {
        if (isToday) {
            Morph(MaterialShapes.Pill, MaterialShapes.Cookie4Sided)
        }else {
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
            isToday -> MaterialTheme.colorScheme.onPrimary
            isSelected -> MaterialTheme.colorScheme.onSecondaryContainer
            isCurrentMonth -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        }, label = "textColor"
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(animatedShape)
            .background(backgroundColor)
            .clickable { onDateClick(date) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}