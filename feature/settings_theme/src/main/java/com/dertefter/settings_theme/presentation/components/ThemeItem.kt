package com.dertefter.settings_theme.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.isCut
import com.dertefter.design.theme.paletteStyle
import com.dertefter.design.theme.specVersion

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    color: Long,
    isSelected: Boolean = false,
){

    val www by animateFloatAsState(
        if (isSelected) 4f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    AppTheme(
        seedColor = color,
        isCut = MaterialTheme.isCut,
        paletteStyle = MaterialTheme.paletteStyle,
        specVersion = MaterialTheme.specVersion
    ) {
        Box(
            modifier = modifier
                .border(
                    width = www.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primary
                )
                .clip(MaterialTheme.shapes.medium)
                .clickable(
                    onClick = onClick
                )
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .size(64.dp)
        ){


            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Row(
                    modifier = Modifier
                        .weight(1f)
                ){
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                ){
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }





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
            isSelected = true
        )
    }
}
