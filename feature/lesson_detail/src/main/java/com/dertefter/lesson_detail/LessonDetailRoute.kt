package com.dertefter.lesson_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dertefter.lesson_detail.presentation.Event
import com.dertefter.lesson_detail.presentation.LessonDetailScreen
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LessonDetailRoute(
    name: String,
    type: String? = null,
    aud: String? = null,
    personIds: List<Long> = emptyList(),
    startTime: LocalTime,
    endTime: LocalTime,
    date: LocalDate,
    viewModel: LessonDetailViewModel = hiltViewModel(),
) {

    val persons by viewModel.persons.collectAsStateWithLifecycle()

    LaunchedEffect(personIds) {
        viewModel.loadPersons(personIds)
    }

    LessonDetailScreen(
        persons = persons,
        name = name,
        type = type,
        aud = aud,
        startTime = startTime,
        endTime = endTime,
        date = date,
        onPersonClick = { personId ->
            viewModel.openPerson(personId = personId)
        }
    )

}
