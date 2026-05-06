package com.dertefter.search_group.presentation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextButton
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

    val voicePromptString = stringResource(R.string.search_group_voice_prompt)

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
                        text = stringResource(R.string.search_group_search_group)
                    ) },
                    leadingIcon = { Icon(Icons.Search, contentDescription = stringResource(R.string.search_group_search_group)) },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    onEvent(Event.OnSearchQueryChanged(""))
                                }
                            ) {
                                Icon(Icons.Close, contentDescription = stringResource(R.string.search_group_clear))
                            }
                        } else {
                            IconButton(
                                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                                onClick = {
                                    try {
                                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                            putExtra(RecognizerIntent.EXTRA_PROMPT, voicePromptString)
                                        }
                                        speechRecognizerLauncher.launch(intent)
                                    } catch (_: ActivityNotFoundException) { }
                                }
                            ) {
                                Icon(Icons.Mic, contentDescription = stringResource(R.string.search_group_voice_recogintion))
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.circleShape(),
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


                AnimatedVisibility(
                    visible =  uiState.groupHistory.isNotEmpty()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.defaultScreenPadding)
                            .clip(MaterialTheme.shapes.largeIncreased)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .fillMaxWidth(),
                    ) {

                        Text(
                            text = stringResource(R.string.search_group_history),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.titleMediumEmphasized,
                            modifier = Modifier
                                .padding(top = MaterialTheme.spacing.extraLarge)
                                .padding(horizontal = MaterialTheme.spacing.extraLarge)
                        )

                        Spacer(
                            modifier = Modifier.height(MaterialTheme.spacing.large)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.extraLarge,),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            items(
                                items = uiState.groupHistory,
                                key = { it.name }
                            ) { group ->
                                GroupHistoryItem(
                                    modifier = Modifier.animateItem(),
                                    group = group,
                                    onClick = {
                                        onEvent(Event.OnSelectGroup(group))
                                    },
                                    onRemove = {
                                        onEvent(Event.OnRemoveGroupFromHistory(group))
                                    }
                                )
                            }
                        }
                        TextButton(
                            onClick = {
                                onEvent(
                                    Event.OnClearGroupHistory
                                )
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(stringResource(R.string.search_group_clear_all))
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
                                text = stringResource(R.string.search_group_nothing_searched),
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
    AppTheme () {

        val l = listOf(
            GroupDto("ПММ-51"),
            GroupDto("ПММ-52"),
            GroupDto("ПМ-11"),
            GroupDto("ПМ-12"),
            GroupDto("ПММ-53"),
            GroupDto("ПММ-54"),
            GroupDto("ПМ-13"),
            GroupDto("ПМ-14"),
            GroupDto("ПММ-55"),
            GroupDto("ПММ-56"),
            GroupDto("ПМ-15"),
            GroupDto("ПМ-16"),
            GroupDto("ПММ-57"),
            GroupDto("ПММ-58"),
            GroupDto("ПМ-17"),
            GroupDto("ПМ-18"),
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
