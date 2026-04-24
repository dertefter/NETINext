package com.dertefter.person_gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.repository.PersonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonGalleryViewModel @Inject constructor(
    private val personsRepository: PersonsRepository,
) : ViewModel() {
    fun updatePersons(ids: List<Long>) {
        viewModelScope.launch {
            for (personId in ids) {
                personsRepository.updatePersonDetail(personId)
            }
        }
    }

    fun getAvatarUrlFlow(personId: Long): Flow<String> =
        personsRepository.getPersonDetailFlowById(personId).map { it?.avatarUrl ?: "" }
}
