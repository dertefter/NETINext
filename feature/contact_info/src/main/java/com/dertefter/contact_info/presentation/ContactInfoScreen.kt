package com.dertefter.contact_info.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dertefter.contact_info.domain.toContactInfo
import com.dertefter.design.components.text_fields.TextFieldItem
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.lkDto
import com.dertefter.design.components.PullToRefreshIndicator
import com.dertefter.design.components.appbar.AppToolbar
import com.dertefter.design.components.buttons.AppNavigationIcon
import com.dertefter.design.components.common.ErrorCard
import com.dertefter.design.icons.Icons
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import com.dertefter.design.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactInfoScreen(
    onEvent: (Event) -> Unit,
    uiState: UiState,
    savingState: SavingState,
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Scaffold(
        topBar = {
            AppToolbar(
                title = "Личные данные",
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    AppNavigationIcon(
                        onClick = {
                            onEvent(Event.OnNavigateUp)
                        })
                },
            )
    }, floatingActionButton = {
        AnimatedVisibility(savingState.isSaveEnabled) {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                onClick = {
                onEvent(Event.OnSave)
            },
                icon = {
                Crossfade(savingState.isLoading) { isLoading ->
                    if (isLoading) {
                        LoadingIndicator()
                    } else {
                        Icon(Icons.Save, null)
                    }
                }


            },
                text = { Text("Сохранить") },
                expanded = (!savingState.isLoading)
            )
        }
    }) { contentPadding ->

        val state = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = state,
            isRefreshing = uiState.isLoading,
            onRefresh = { onEvent(Event.OnRequestUpdate) },
            indicator = {
                PullToRefreshIndicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .align(Alignment.TopCenter),
                    state = state,
                    isRefreshing = uiState.isLoading
                )
            }) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.defaultScreenPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {

                uiState.error?.let {
                    item {
                        ErrorCard(
                            title = "Ошибка обновления",
                            message = null,
                            onRetry = { onEvent(Event.OnRequestUpdate) })
                    }

                }


                uiState.contactInfo?.let { contactInfo ->

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = MaterialTheme.spacing.small),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        )
                        {

                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.medium,
                                    )
                                    .padding(horizontal = MaterialTheme.rounding.large)
                                    .fillMaxWidth(),
                                text = "Контактная информация",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMediumEmphasized,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,

                                )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.large),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            ){

                                TextFieldItem(
                                    value = contactInfo.email.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnEmailChange(it))
                                    },
                                    hint = "Электронная почта",
                                    icon = Icons.Mail,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                )

                                TextFieldItem(
                                    value = contactInfo.mobilePhoneNumber.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnMobilePhoneNumberChange(it))
                                    },
                                    hint = "Номер телефона",
                                    icon = Icons.Call,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    maxSymbols = 17,
                                    isError = uiState.validateFails.contains(ValidateFail.PHONE)
                                )

                                TextFieldItem(
                                    value = contactInfo.address.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnAdressChange(it))
                                    },
                                    hint = "Адрес",
                                    icon = Icons.Home,
                                )

                                TextFieldItem(
                                    value = contactInfo.snils.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnSnilsChange(it))
                                    },
                                    hint = "СНИЛС",
                                    icon = Icons.Subtitles,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    maxSymbols = 14,
                                    isError = uiState.validateFails.contains(ValidateFail.SNILS)
                                )

                                TextFieldItem(
                                    value = contactInfo.oms.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnOmsChange(it))
                                    },
                                    hint = "Полис ОМС",
                                    icon = Icons.HealthCross,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                            }



                            Spacer(
                                modifier = Modifier.height(MaterialTheme.spacing.medium)
                            )

                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.medium,
                                    )
                                    .padding(horizontal = MaterialTheme.rounding.large)
                                    .fillMaxWidth(),
                                text = "Ссылки на профили в соцсетях",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMediumEmphasized,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,

                                )

                            Column(
                                modifier = Modifier
                                    .padding(bottom = MaterialTheme.spacing.defaultScreenPadding)
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.large),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            )
                            {

                                TextFieldItem(
                                    value = contactInfo.vk.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnVkChange(it))
                                    },
                                    hint = "ВКонтакте",
                                    icon = Icons.VK,
                                )

                                TextFieldItem(
                                    value = contactInfo.telegram.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnTelegramChange(it))
                                    },
                                    hint = "Telegram",
                                    icon = Icons.TG,
                                )

                                TextFieldItem(
                                    value = contactInfo.leaderId.orEmpty(),
                                    onValueChange = {
                                        onEvent(Event.OnLeaderIdChange(it))
                                    },
                                    hint = "Leader-ID",
                                    icon = Icons.LeaderID,
                                )

                            }
                        }
                    }
                }

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactInfoScreenPreview() {
    AppTheme () {
        ContactInfoScreen(
             onEvent = {}, uiState = UiState(
                contactInfo = ContactInfoDto(
                    name = "Иван",
                    surname = "Иванов",
                    patronymic = "Иванович",
                    symGroup = "АБВ-123",
                    email = "ivanov@example.com",
                    address = "г. Новосибирск, ул. Пушкина, д. 10",
                    mobilePhoneNumber = "+7 999 123 45 67",
                    snils = "123-456-789 01",
                    oms = "1234567890123456",
                    vk = "https://vk.com/id1",
                    telegram = "@ivanov",
                    leaderId = "12345",
                    lksList = listOf(
                        lkDto(id = 1, title = "ЛК 1", subtitle = "Описание 1", isSelected = true),
                        lkDto(id = 2, title = "ЛК 2", subtitle = "Описание 2", isSelected = false)
                    )
                ).toContactInfo(), isLoading = false
            ), savingState = SavingState(true, true)
        )
    }
}
