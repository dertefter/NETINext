package com.dertefter.settings_labs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.settings_labs.presentation.Event
import com.dertefter.settings_labs.presentation.UiState
import com.dertefter.settings_labs.usecase.GetSelectedRemoteDataSourceUseCase
import com.dertefter.settings_labs.usecase.NavigateUpUseCase
import com.dertefter.settings_labs.usecase.SetSelectedRemoteDataSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsLabsViewModel @Inject constructor(
    getSelectedRemoteDataSourceUseCase: GetSelectedRemoteDataSourceUseCase,
    private val setSelectedRemoteDataSourceUseCase: SetSelectedRemoteDataSourceUseCase,
    private val navigateUpUseCase: NavigateUpUseCase
    ) : ViewModel() {

    private val selectedRemoteDataSource = getSelectedRemoteDataSourceUseCase()
        
    val uiState: StateFlow<UiState> = selectedRemoteDataSource.map {
        UiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState(PreferredRemoteSource.AUTO)
    )

    fun onEvent(event: Event) {
        when (event) {

            is Event.OnSelectPreferredDataSource -> {
                viewModelScope.launch {
                    setSelectedRemoteDataSourceUseCase(event.p)
                }
            }

            is Event.OnNavigateBack -> {
                navigateUpUseCase()
            }
        }
    }


}
