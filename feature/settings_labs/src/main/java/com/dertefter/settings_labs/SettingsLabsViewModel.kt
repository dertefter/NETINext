package com.dertefter.settings_labs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.repository.SettingsRepository
import com.dertefter.navigation.Navigator
import com.dertefter.settings_labs.presentation.Event
import com.dertefter.settings_labs.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsLabsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val navigator: Navigator
    ) : ViewModel() {

    val selectedRemoteDataSource = settingsRepository.selectedRemoteDataSource
        
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
                    settingsRepository.setSelectedRemoteDataSource(event.p)
                }
            }

            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }

            else -> {}

        }
    }


}
