package com.dertefter.search_person.presentation.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.imageLoader
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@Composable
fun PersonItem(
    modifier: Modifier = Modifier,
    name: String,
    avatarUrl: String? = null,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    onClick: () -> Unit,

    ){

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding()
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.defaultScreenPadding
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
                .background(MaterialTheme.colorScheme.secondaryContainer)
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
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }

        Text(
            text = name,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MessageCardPreview() {
    AppTheme {
        PersonItem(
            name = "Иван Иванов",
            onClick = {}
        )
    }
}
