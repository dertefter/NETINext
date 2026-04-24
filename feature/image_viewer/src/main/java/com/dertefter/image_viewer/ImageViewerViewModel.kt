package com.dertefter.image_viewer

import androidx.lifecycle.ViewModel
import com.dertefter.image_viewer.presentation.Event
import com.dertefter.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    private val navigator: Navigator
) : ViewModel() {

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnNavigateBack -> {
                navigator.navigateUp()
            }
        }
    }

}
