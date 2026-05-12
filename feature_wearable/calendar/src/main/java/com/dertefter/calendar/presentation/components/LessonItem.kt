package com.dertefter.calendar.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.dertefter.design.components.schedule.SimpleShip

@Composable
fun LessonItem(
    modifier: Modifier = Modifier,
    title: String,
    aud: String?,
    type: String?,
    isHighlight: Boolean = false,
    personIds: List<Long> = emptyList(),
){

    val titleFontWeight by animateIntAsState(
        targetValue = if (isHighlight) 800 else 400,
        label = "titleWeight"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isHighlight)
            MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer,
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isHighlight)
            MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurface,
        label = "textColor"
    )

    Column(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {



        Text(
            text = title,
            color = textColor,
            minLines = 2,
            fontWeight = FontWeight(titleFontWeight)
        )


        FlowRow(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ){

            aud?.let { aud ->
                if (aud.isNotEmpty()){
                    SimpleShip(
                        label = aud,
                        isHighlight = isHighlight
                    )
                }

            }

            type?.let { type ->
                if (type.isNotEmpty()){
                    SimpleShip(
                        label = type,
                        isHighlight = isHighlight
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true, device = "id:wearos_xl_round")
@Composable
fun LessonItemPreview(){
    MaterialTheme {
        LessonItem(
            modifier = Modifier.padding(10.dp),
            title = "sdjcsjcdj sdjcsjcdjk sdjcsjcdjksdjcsjcdjksdjcsjcdjk мsdjcsjcdjk sdjcsjcdjk sdjcsjcdjk sdjcsjcdjk sdjcsjcdjkk",
            aud = "2-318",
            type = "лабораторная",
            personIds = listOf(1,2,3)
        )
    }
}


@Preview(showBackground = true, device = "id:wearos_xl_round")
@Composable
fun LessonItemPreview2(){
    MaterialTheme {
        LessonItem(
            modifier = Modifier.padding(10.dp),
            title = "sdjcsjc djidvid djkvdjkdv dvjdvjd dsjd djk",
            aud = "4444",
            type = "практикака",
            isHighlight = true
        )
    }
}
