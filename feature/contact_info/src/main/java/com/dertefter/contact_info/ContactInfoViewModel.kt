package com.dertefter.contact_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.contact_info.domain.ContactInfo
import com.dertefter.contact_info.domain.toContactInfo
import com.dertefter.contact_info.presentation.Event
import com.dertefter.contact_info.presentation.SavingState
import com.dertefter.contact_info.presentation.UiState
import com.dertefter.contact_info.presentation.ValidateFail
import com.dertefter.contact_info.util.PhoneFormatter
import com.dertefter.contact_info.util.SnilsFormatter
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.data.repository.ContactInfoRepository
import com.dertefter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactInfoViewModel @Inject constructor(
    private val userInfoRepository: ContactInfoRepository,
    private val contactInfoRepository: ContactInfoRepository,
    private val navigator: Navigator
) : ViewModel() {


    private val _realContactInfo = MutableStateFlow<ContactInfo?>(null)

    private val _contactInfo = MutableStateFlow<ContactInfo?>(null)

    private val _isLoading = MutableStateFlow(false)
    private val _isSaving = MutableStateFlow(false)

    private val _validateFails = MutableStateFlow<List<ValidateFail>>(emptyList())
    private val _error = MutableStateFlow<AppError?>(null)

    init {
        viewModelScope.launch {
            _contactInfo.value = contactInfoRepository.getContactInfo().first()?.toContactInfo()
        }
        updateUserInfo()
    }

    val state: StateFlow<UiState> = combine(
        _contactInfo,
        _isLoading,
        _error,
        _validateFails,
    ) { contactInfo, isLoading, error, validateFails ->
        UiState(
            contactInfo = contactInfo,
            isLoading = isLoading,
            error = error,
            validateFails = validateFails
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState()
    )

    val savingState: StateFlow<SavingState> = combine(
        _contactInfo,
        _realContactInfo,
        _validateFails,
        _isSaving
    ) { contactInfo, realContactInfo, validateFails, isSaving ->
        SavingState(
            isLoading = isSaving,
            isSaveEnabled = contactInfo != null && realContactInfo != null && contactInfo != realContactInfo && validateFails.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SavingState()
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnNavigateUp -> {
                navigator.navigateUp()
            }

            is Event.OnSave -> {
                viewModelScope.launch {
                    val contactInfo = _contactInfo.value ?: return@launch
                    _error.value = null
                    _isSaving.value = true
                    contactInfoRepository.saveContactInfo(
                        email = contactInfo.email,
                        address = contactInfo.address,
                        phone = contactInfo.mobilePhoneNumber,
                        snils = contactInfo.snils,
                        oms = contactInfo.oms,
                        vk = contactInfo.vk,
                        tg = contactInfo.telegram,
                        leader = contactInfo.leaderId,
                    ).onSuccess {
                        _realContactInfo.value = it.toContactInfo()
                        _contactInfo.value = it.toContactInfo()
                    }.onFailure {
                            _error.value = it.toAppError()
                        }
                    _isSaving.value = false
                }

            }

            is Event.OnRequestUpdate -> {
                updateUserInfo()
            }
            is Event.OnEmailChange -> {
                _contactInfo.value = _contactInfo.value?.copy(email = event.email)
            }

            is Event.OnOmsChange -> {
                _contactInfo.value = _contactInfo.value?.copy(oms = event.oms)
            }

            is Event.OnSnilsChange -> {
                _contactInfo.update { current ->
                    current?.copy(snils = event.snils)?.copy()
                }

                val formattedSnils = SnilsFormatter.format(event.snils)
                _contactInfo.update { current ->
                    current?.copy(snils = formattedSnils)
                }
                val isValid = Regex("^\\d{3}-\\d{3}-\\d{3} \\d{2}$").matches(formattedSnils) || formattedSnils.isEmpty()
                _validateFails.value = if (isValid) {
                    _validateFails.value - ValidateFail.SNILS
                } else {
                    (_validateFails.value + ValidateFail.SNILS).distinct()
                }
            }


            is Event.OnMobilePhoneNumberChange -> {
                val formattedPhone = PhoneFormatter.format(event.mobilePhoneNumber)
                _contactInfo.value = _contactInfo.value?.copy(mobilePhoneNumber = formattedPhone)
                val isValid = formattedPhone.count { it.isDigit() } == 11 || formattedPhone.isEmpty()
                _validateFails.value = if (isValid) {
                    _validateFails.value - ValidateFail.PHONE
                } else {
                    (_validateFails.value + ValidateFail.PHONE).distinct()
                }
            }

            is Event.OnAdressChange -> {
                _contactInfo.value = _contactInfo.value?.copy(address = event.address)
            }
            is Event.OnVkChange -> {
                _contactInfo.value = _contactInfo.value?.copy(vk = event.vk)
            }
            is Event.OnTelegramChange -> {
                _contactInfo.value = _contactInfo.value?.copy(telegram = event.telegram)
            }
            is Event.OnLeaderIdChange -> {
                _contactInfo.value = _contactInfo.value?.copy(leaderId = event.leaderId)
            }
        }
    }

    private fun updateUserInfo(){
        viewModelScope.launch {
            _error.value = null
            setLoading(true)
            contactInfoRepository.updateContactInfo()
                .onSuccess {
                    _realContactInfo.value = it.toContactInfo()
                    _contactInfo.value = it.toContactInfo()
                }
                .onFailure {
                    _error.value = it.toAppError()
                }
            setLoading(false)
        }
    }

    private fun setLoading(n: Boolean){
        _isLoading.value = n
    }

}
