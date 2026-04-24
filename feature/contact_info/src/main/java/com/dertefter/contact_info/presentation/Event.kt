package com.dertefter.contact_info.presentation

sealed class Event {
    data class OnAdressChange(val address: String) : Event()

    data class OnSnilsChange(val snils: String) : Event()

    data class OnOmsChange(val oms: String) : Event()
    data class OnEmailChange(val email: String) : Event()
    data class OnMobilePhoneNumberChange(val mobilePhoneNumber: String) : Event()
    data class OnVkChange(val vk: String) : Event()
    data class OnTelegramChange(val telegram: String) : Event()
    data class OnLeaderIdChange(val leaderId: String) : Event()

    object OnRequestUpdate : Event()

    object OnSave : Event()

    object OnNavigateUp : Event()

}
