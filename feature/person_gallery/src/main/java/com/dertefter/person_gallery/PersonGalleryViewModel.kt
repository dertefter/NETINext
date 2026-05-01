package com.dertefter.person_gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.person_gallery.usecase.GetAvatarUrlFlowUseCase
import com.dertefter.person_gallery.usecase.UpdatePersonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonGalleryViewModel @Inject constructor(
    private val updatePersonDetailUseCase: UpdatePersonDetailUseCase,
    private val getAvatarUrlFlowUseCase: GetAvatarUrlFlowUseCase,
) : ViewModel() {
    fun updatePersons(ids: List<Long>) {
        viewModelScope.launch {
            for (personId in ids) {
                updatePersonDetailUseCase(personId)
            }
        }
    }

    fun getAvatarUrlFlow(personId: Long): Flow<String> =
        getAvatarUrlFlowUseCase(personId)
}
