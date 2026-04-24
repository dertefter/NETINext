package com.dertefter.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
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
import coil.compose.LocalImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.compose.SubcomposeAsyncImageContent
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserInfoCard(

    modifier: Modifier = Modifier,
    login: String,
    name: String? = null,
    avatarUrl: String? = null,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    onClick: () -> Unit,
    onImageClick: () -> Unit = {}
) {

    Row {
        Row(
            modifier = modifier
                .clip(MaterialTheme.circleShape())
                .clickable(
                    onClick = onClick
                )
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SubcomposeAsyncImage(
                model = avatarUrl,
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.2f),
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .clip(MaterialTheme.circleShape())
                    .background(MaterialTheme.colorScheme.surface)
                    .size(74.dp)
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = Icons.UserFilled,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier
                                .clickable(
                                    onClick = onImageClick
                                )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(end = MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = name ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = login,
                    style = MaterialTheme.typography.labelMediumEmphasized,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                modifier = Modifier
                    .padding(end = MaterialTheme.spacing.extraLarge)
                    .size(24.dp),
                imageVector = Icons.EditFilled,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }
    }


}

@Preview(showBackground = true)
@Composable
private fun UserInfoCardPreview() {
    AppTheme {
        UserInfoCard(
            name = "dddd",
            login = "ivanov.2023",
            imageLoader = ImageLoader(LocalContext.current),
            onClick = {}
        )
    }
}
