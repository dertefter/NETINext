package com.dertefter.search_group.presentation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.data.dto.schedule.GroupDto
import com.dertefter.design.components.common.ErrorLarge
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.circleShape
import com.dertefter.design.theme.spacing
import com.dertefter.search_group.R
import com.dertefter.search_group.presentation.componets.GroupHistoryItem
import com.dertefter.search_group.presentation.componets.GroupItem
import com.gigamole.composefadingedges.verticalFadingEdges
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchGroupScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
) {
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            spokenText?.let {
                onEvent(Event.OnSearchQueryChanged(it))
            }
        }
    }

    Scaffold(
      containerColor = Color.Transparent,
        topBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                        .fillMaxWidth(),
                    value = uiState.query,
                    onValueChange = { onEvent(Event.OnSearchQueryChanged(it)) },
                    placeholder = {
                        Text(
                        text = stringResource(R.string.search_group)
                    ) },
                    leadingIcon = { Icon(Icons.Search, contentDescription = stringResource(R.string.search_group)) },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    onEvent(Event.OnSearchQueryChanged(""))
                                }
                            ) {
                                Icon(Icons.Close, contentDescription = stringResource(R.string.clear))
                            }
                        } else {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Произнесите название группы")
                                    }
                                    speechRecognizerLauncher.launch(intent)
                                }
                            ) {
                                Icon(Icons.Mic, contentDescription = stringResource(R.string.voice_recogintion))
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    isError = uiState.error != null,
                    colors = OutlinedTextFieldDefaults.colors().copy(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                )

                if (uiState.groupHistory.isNotEmpty()){
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.defaultScreenPadding),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item {
                            Icon(
                                imageVector = Icons.History,
                                contentDescription = stringResource(R.string.history),
                                modifier = Modifier
                                    .clip(MaterialTheme.circleShape())
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .padding(MaterialTheme.spacing.medium)
                            )
                        }

                        items(uiState.groupHistory) { group ->
                            GroupHistoryItem(
                                group = group,
                                onClick = {
                                    onEvent(Event.OnSelectGroup(group))
                                },
                                {
                                    onEvent(Event.OnRemoveGroupFromHistory(group))
                                }
                            )
                        }
                    }
                }

            }

        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalFadingEdges(
                    length = contentPadding.calculateTopPadding(),
                )
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                ,
        ) {


            if (uiState.groups.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().imePadding()
                ){
                    if (uiState.isLoading){
                        LoadingIndicator(
                            Modifier.align(Alignment.Center)
                        )
                    } else {
                        if (uiState.error != null){
                            ErrorLarge()
                        } else {
                            Text(
                                text = stringResource(R.string.nothing_searched),
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                }
            }

            LazyVerticalGrid(
                contentPadding = contentPadding,
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(vertical = MaterialTheme.spacing.small)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                items(uiState.groups) { group ->
                    GroupItem(
                        group = group,
                        onClick = {
                            onEvent(Event.OnSelectGroup(group))
                        }
                    )
                }
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun SearchGroupScreenPreview() {
    AppTheme (
        isCut = true
    ) {

        val l = listOf(
            GroupDto("ПММ-51"),
            GroupDto("ПММ-52"),
            GroupDto("ПМ-11"),
            GroupDto("ПМ-12"),
            GroupDto("ПММ-51"),
            GroupDto("ПММ-52"),
            GroupDto("ПМ-11"),
            GroupDto("ПМ-12"),
            GroupDto("ПММ-51"),
            GroupDto("ПММ-52"),
            GroupDto("ПМ-11"),
            GroupDto("ПМ-12"),
            GroupDto("ПММ-51"),
            GroupDto("ПММ-52"),
            GroupDto("ПМ-11"),
            GroupDto("ПМ-12"),
        )

        SearchGroupScreen(
            onEvent = {},
            uiState = UiState(
                query = "ПМ",
                groupHistory = l,
                groups = l,
                isLoading = false
            )
        )
    }
}

@Composable
@Preview
fun SearchGroupScreenPreview2() {
    AppTheme {
        SearchGroupScreen(
            onEvent = {},
            uiState = UiState(
                query = "",
                groups = listOf(
                ),
                isLoading = false
            )
        )
    }
}
