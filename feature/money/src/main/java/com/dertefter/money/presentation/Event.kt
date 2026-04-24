package com.dertefter.money.presentation

sealed class Event {
    object OnUpdateYears : Event()
    data class OnUpdateMoneyForYear(val year: String) : Event()
    object OnNavigateBack : Event()
}
