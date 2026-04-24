package com.dertefter.person_gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.dertefter.design.icons.Icons

import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape

@Composable
fun PersonGallery(
    personIds: List<Long>,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 28.dp,
    overlapAmount: Dp = 10.dp,
    borderColor: Color = Color.White,
    viewModel: PersonGalleryViewModel = hiltViewModel(),
) {
    LaunchedEffect(personIds) { viewModel.updatePersons(personIds) }

    val avatarUrls = personIds.map { personId ->
        viewModel.getAvatarUrlFlow(personId).collectAsState("").value
    }

    PersonGalleryContent(
        avatarUrls = avatarUrls,
        modifier = modifier,
        avatarSize = avatarSize,
        overlapAmount = overlapAmount,
        borderColor = borderColor
    )
}

@Composable
fun PersonGalleryContent(
    avatarUrls: List<String>,
    modifier: Modifier = Modifier,
    avatarSize: Dp = 28.dp,
    overlapAmount: Dp = 10.dp,
    borderColor: Color = Color.White,
) {
    Box(modifier = modifier) {
        avatarUrls.forEachIndexed { index, avatarUrl ->
            SubcomposeAsyncImage(
                model = avatarUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Icon(
                        imageVector = Icons.UserFilled,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                error = {
                    Icon(
                        imageVector = Icons.UserFilled,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .padding(start = if (index == 0) 0.dp else (avatarSize - overlapAmount) * index)
                    .size(avatarSize)
                    .border(2.dp, borderColor, MaterialTheme.circleShape())
                    .clip(MaterialTheme.circleShape())
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonGalleryPreview() {
    AppTheme(
        isCut = true
    ) {
        PersonGalleryContent(
            avatarUrls = listOf("", "", ""),
            modifier = Modifier.padding(16.dp)
        )
    }
}
