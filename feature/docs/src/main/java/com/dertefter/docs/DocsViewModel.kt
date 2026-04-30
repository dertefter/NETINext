package com.dertefter.docs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.repository.DocsRepository
import com.dertefter.docs.presentation.Event
import com.dertefter.docs.presentation.UiState
import com.dertefter.navigation.Navigator
import com.dertefter.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Route
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DocsViewModel @Inject constructor(
    private val docsRepository: DocsRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _isUpdating = MutableStateFlow(false)
    private val _isError =  MutableStateFlow(false)

    private val _docs = docsRepository.getDocsList()


    val uiState: StateFlow<UiState> = combine(
        _docs,
        _isUpdating,
        _isError
    ) { docs, isUpdating, isError ->
        UiState(
            docsList = docs,
            isLoading = isUpdating,
            isError = isError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnUpdate -> {
                updateDocsList()
            }
            is Event.OnOpenDocDetail -> {
                val item = event.docItem
                navigator.openAsBottomSheet(
                    Routes.DocDetail(
                        type = item.type,
                        date = item.date,
                        status = item.status,
                        person = item.person,
                        comment = item.comment,
                        number = item.number
                    )
                )
            }
            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }
            is Event.OnNavigateToNewDocument -> {
                navigator.navigate(Routes.NewDocument)
            }
        }
    }

    private fun updateDocsList() {
        viewModelScope.launch {
            _isUpdating.update { true }
            _isError.update { false }
            docsRepository.updateDocsList()
                .onFailure { error ->
                    _isError.update { true }
                }
            _isUpdating.update { false }
        }
    }
}
