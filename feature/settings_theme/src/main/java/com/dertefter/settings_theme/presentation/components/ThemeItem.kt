package com.dertefter.settings_theme.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.cornerShape
import com.dertefter.design.theme.isCut
import com.dertefter.design.theme.rounding

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: Long,
    isSelected: Boolean = false,
){

    val cornerRadius by animateDpAsState(
        if (isSelected) MaterialTheme.rounding.large else MaterialTheme.rounding.medium
    )

    val iconAlpha by animateFloatAsState(
        if (isSelected) 1f else 0f
    )

    AppTheme(
        seedColor = color,
        isCut = MaterialTheme.isCut
    ) {
        Box(
            modifier = modifier
                .clip(MaterialTheme.cornerShape(cornerRadius))
                .clickable(
                    onClick = onClick
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
                .size(64.dp)
        ){

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(32.dp)
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .weight(1f)
                        .fillMaxHeight()
                )
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Icon(
                imageVector = Icons.Check,
                contentDescription = null,
                modifier = Modifier
                    .alpha(iconAlpha)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f))
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )


        }



    }
}

@Preview(showBackground = true)
@Composable
fun ThemeItemPreview() {
    AppTheme(
        isCut = false,
    ) {
        ThemeItem(
            color = Color.Red.toArgb().toLong(),
            modifier = Modifier.padding(16.dp),
        )
    }
}
