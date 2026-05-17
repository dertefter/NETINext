package com.dertefter.design.components.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import kotlinx.coroutines.delay

@Composable
fun NoLessons(
    modifier: Modifier = Modifier,
    text: String,
    subtext: String? = null,
    onClick: () -> Unit = {}
) {
    val icons = listOf(
        Icons.Group, Icons.Engineering, Icons.NestEcoLeaf, Icons.Architecture, Icons.SportsEsports, Icons.Microbiology, Icons.Biotech, Icons.School
    )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            currentIndex = (currentIndex + 1) % icons.size
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.circleShape())
                .background(MaterialTheme.colorScheme.primaryContainer)
                .size(54.dp)
             ,
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = icons[currentIndex],
                transitionSpec = {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                },
                label = "IconSlideAnimation"
            ) { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxSize()
                )
            }
        }

        Spacer(
            modifier = Modifier.height(MaterialTheme.spacing.medium)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
        subtext?.let{
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoLessonsPreview() {
    AppTheme(isCut = true) {
        NoLessons(text = "Нет занятий", subtext = "Рлорло тмтл ывтол")
    }
}
