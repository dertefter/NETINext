package com.dertefter.person_detail.presentation.component

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import com.dertefter.person_detail.R
import com.dertefter.person_detail.presentation.Event
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PersonDetailContent(
    modifier: Modifier = Modifier,
    personDetail: PersonDetailDto,
    contentPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    onEvent: (Event) -> Unit = {}

) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .widthIn(max = 400.dp)
            .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Row(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                SubcomposeAsyncImage(
                    model = personDetail.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(
                        horizontalBias = 0f,
                        verticalBias = -0.2f
                    ),
                    modifier = Modifier
                        .clip(MaterialTheme.circleShape())
                        .clickable(
                            onClick = {
                                if (!personDetail.avatarUrl.isNullOrEmpty()){
                                    onEvent(
                                        Event.OnOpenAvatar(personDetail.avatarUrl ?: "")
                                    )
                                }
                            }
                        )
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .size(64.dp)
                )
                {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading, is AsyncImagePainter.State.Error -> {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(44.dp),
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

                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    Text(
                        text = personDetail.name ?: "",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    personDetail.post?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelLargeEmphasized
                        )
                    }
                }

            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {

                if (personDetail.contactInfo.isNotEmpty()) {
                    TitleAndStrings(
                        title = stringResource(R.string.person_detail_contact_info),
                        strings = personDetail.contactInfo,
                        onStringClicked = { string ->
                            coroutineScope.launch {
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("", string)))
                            }

                        }
                    )
                }
                if (personDetail.disciplines.isNotEmpty()) {
                    TitleAndStrings(
                        title = stringResource(R.string.person_detail_subjects),
                        strings = personDetail.disciplines,
                        onStringClicked = { string ->
                            coroutineScope.launch {
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("", string)))
                            }
                        }
                    )
                }
                if (personDetail.profiles.isNotEmpty()) {
                    TitleAndStrings(
                        title = stringResource(R.string.person_detail_profiles),
                        strings = personDetail.profiles,
                        onStringClicked = { string ->
                            coroutineScope.launch {
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText("", string)))
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
private fun PersonDetailContentPreview() {
    AppTheme {
        PersonDetailContent(
            personDetail = PersonDetailDto(
                personId = 1,
                name = "Иван Иванов",
                avatarUrl = null,
                post = "Профессор кафедры математики",
                contactInfo = listOf("ivanov@university.edu", "+7 (999) 123-45-67"),
                disciplines = listOf("Высшая математика", "Теория вероятностей"),
                profiles = listOf("https://scholar.google.com/ivanov"),
                hasTimetable = true
            ),
            contentPadding = PaddingValues(0.dp),
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        )
    }
}
