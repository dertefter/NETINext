package com.dertefter.messages.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.imageLoader
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    name: String,
    message: String,
    avatarUrl: String?,
    date: LocalDateTime,
    isRead: Boolean = true,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    cardShape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit,

    ){

    val formattedDate = remember(date) {
        val now = LocalDate.now()
        val messageDate = date.toLocalDate()
        when {
            messageDate == now -> "Сегодня"
            messageDate == now.minusDays(1) -> "Вчера"
            messageDate.year == now.year -> {
                date.format(DateTimeFormatter.ofPattern("d MMM", Locale.getDefault()))
            }

            else -> {
                date.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()))
            }
        }
    }

    val fontWeight = if (isRead) {
        400
    } else {
        600
    }

    val bgColor = if (isRead) {
        MaterialTheme.colorScheme.surfaceContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHighest
    }


    Row(
        modifier = modifier
            .clip(cardShape)
            .clickable(onClick = onClick)
            .background(bgColor)
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.large
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
    )
    {
        SubcomposeAsyncImage(
            model = avatarUrl,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f),
            modifier = Modifier
                .clip(MaterialTheme.circleShape())
                .background(MaterialTheme.colorScheme.secondary)
                .size(40.dp)
        )
        {
            when (painter.state) {
                is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp),
                            imageVector = Icons.UserFilled,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = MaterialTheme.spacing.small)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight(fontWeight),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight(fontWeight),
                )

            }

            Text(
                text = message,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight(fontWeight),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}