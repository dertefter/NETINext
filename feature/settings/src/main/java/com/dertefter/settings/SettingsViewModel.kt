package com.dertefter.settings

import androidx.lifecycle.ViewModel
import com.dertefter.data.repository.GroupsRepository
import com.dertefter.data.repository.ScheduleRepository
import com.dertefter.navigation.Navigator
import com.dertefter.settings.presentation.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnNavigateTo -> {
                navigator.navigate(event.route)
            }
            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }
        }
    }

}
