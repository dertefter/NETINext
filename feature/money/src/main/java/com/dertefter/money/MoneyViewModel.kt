package com.dertefter.money

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.common.AppError
import com.dertefter.data.common.toAppError
import com.dertefter.money.presentation.Event
import com.dertefter.money.presentation.UiState
import com.dertefter.money.usecase.GetMoneyForYearUseCase
import com.dertefter.money.usecase.GetMoneyYearsUseCase
import com.dertefter.money.usecase.NavigateUpUseCase
import com.dertefter.money.usecase.UpdateMoneyForYearUseCase
import com.dertefter.money.usecase.UpdateMoneyYearsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MoneyViewModel @Inject constructor(
    getMoneyYearsUseCase: GetMoneyYearsUseCase,
    private val getMoneyForYearUseCase: GetMoneyForYearUseCase,
    private val updateMoneyYearsUseCase: UpdateMoneyYearsUseCase,
    private val updateMoneyForYearUseCase: UpdateMoneyForYearUseCase,
    private val navigateUpUseCase: NavigateUpUseCase
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _error = MutableStateFlow<AppError?>(null)

    private val _years = getMoneyYearsUseCase()

    private val _moneyData = _years.flatMapLatest { years ->
        if (years.isEmpty()) {
            flowOf(emptyMap())
        } else {
            combine(years.map { year ->
                getMoneyForYearUseCase(year).map { year to it }
            }) { it.toMap() }
        }
    }

    val uiState: StateFlow<UiState> = combine(
        _years,
        _moneyData,
        _isUpdating,
        _error
    ) { years, moneyData, isUpdating, error ->
        UiState(
            years = years,
            moneyData = moneyData,
            isLoading = isUpdating,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    init {
        updateMoneyYears()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdateYears -> {
                updateMoneyYears()
            }
            is Event.OnUpdateMoneyForYear -> {
                updateMoneyForYear(event.year)
            }
            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }
        }
    }

    private fun updateMoneyYears() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            updateMoneyYearsUseCase()
                .onFailure { error ->
                    _error.update { error.toAppError() }
                }
            _isUpdating.update { false }
        }
    }

    private fun updateMoneyForYear(year: String) {
        viewModelScope.launch {
            _isUpdating.update { true }
            _error.update { null }
            updateMoneyForYearUseCase(year)
                .onFailure { error ->
                    _error.update { error.toAppError() }
                }
            _isUpdating.update { false }
        }
    }
}
