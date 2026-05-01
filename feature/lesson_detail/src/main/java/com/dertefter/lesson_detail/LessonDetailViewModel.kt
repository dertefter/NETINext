package com.dertefter.lesson_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.lesson_detail.usecase.GetPersonsByIdsUseCase
import com.dertefter.lesson_detail.usecase.NavigateToPersonDetailUseCase
import com.dertefter.lesson_detail.usecase.UpdatePersonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonDetailViewModel @Inject constructor(
    private val getPersonsByIdsUseCase: GetPersonsByIdsUseCase,
    private val updatePersonDetailUseCase: UpdatePersonDetailUseCase,
    private val navigateToPersonDetailUseCase: NavigateToPersonDetailUseCase,
) : ViewModel() {

    private val _persons = MutableStateFlow<List<PersonDetailDto>>(emptyList())
    val persons: StateFlow<List<PersonDetailDto>> = _persons

    private var loadJob: Job? = null

    fun loadPersons(ids: List<Long>) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val distinctIds = ids.distinct()
            launch {
                getPersonsByIdsUseCase(distinctIds).collect { personsList ->
                    _persons.value = personsList
                }
            }
            distinctIds.forEach { id ->
                launch {
                    updatePersonDetailUseCase(id)
                }
            }
        }
    }


    fun openPerson(personId: Long){
        navigateToPersonDetailUseCase(personId)
    }

}
