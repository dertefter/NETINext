package com.dertefter.home.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.components.schedule.SimpleShip
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.spacing
import com.dertefter.person_gallery.PersonGallery

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LessonItem(
    modifier: Modifier = Modifier,
    title: String,
    aud: String?,
    type: String?,
    isHighlight: Boolean = false,
    personIds: List<Long> = emptyList(),
    onClick: () -> Unit = {}
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
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth()
    ) {


        val density = LocalDensity.current
        val inlineContent = remember(personIds) {
            if (personIds.isNotEmpty()) {
                val galleryWidth = 36.dp + (personIds.size - 1).coerceAtLeast(0).dp * 20
                mapOf(
                    "gallery" to InlineTextContent(
                        Placeholder(
                            width = with(density) { (galleryWidth).toSp() },
                            height = with(density) { 28.dp.toSp() },
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) {
                        PersonGallery(
                            personIds = personIds,
                            borderColor = backgroundColor
                        )
                    }
                )
            } else {
                emptyMap()
            }
        }

        Text(
            text = buildAnnotatedString {
                if (personIds.isNotEmpty()) {
                    appendInlineContent("gallery", "[gallery]")
                }
                append(title)
            },
            inlineContent = inlineContent,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            minLines = 2,
            fontWeight = FontWeight(titleFontWeight)
        )



        Spacer(
            Modifier.height(MaterialTheme.spacing.medium)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
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

@Preview(showBackground = true)
@Composable
fun LessonItemPreview(){
    AppTheme {
        LessonItem(
            modifier = Modifier.padding(10.dp),
            title = "sdjcsjcdjk",
            aud = "4444",
            type = "практика",
            personIds = listOf(1,2,3)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LessonItemPreview2(){
    AppTheme {
        LessonItem(
            modifier = Modifier.padding(10.dp),
            title = "sdjcsjcdjk",
            aud = "4444",
            type = "практикака",
            isHighlight = true
        )
    }
}
