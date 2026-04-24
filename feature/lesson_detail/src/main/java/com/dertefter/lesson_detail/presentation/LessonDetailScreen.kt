package com.dertefter.lesson_detail.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.design.components.schedule.SimpleShip
import com.dertefter.design.theme.spacing
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dertefter.design.theme.AppTheme
import com.dertefter.design.theme.rounding
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LessonDetailScreen(
    name: String,
    type: String? = null,
    aud: String? = null,
    startTime: LocalTime,
    endTime: LocalTime,
    date: LocalDate,
    persons: List<PersonDetailDto> = emptyList(),
    onPersonClick: (Long) -> (Unit) = {}
) {


    val isToday = remember(date) { date == LocalDate.now() }

    var tick by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val isNow = remember(tick, isToday, startTime, endTime) {
        isToday && !LocalTime.now().isBefore(startTime) && !LocalTime.now().isAfter(endTime)
    }



    LaunchedEffect(isToday, startTime, endTime) {
        while (true) {
            tick = System.currentTimeMillis()
            delay(2000)
        }
    }

    val fontWeight = when {
        isNow -> 800
        else -> 400
    }



    Scaffold(
      containerColor = Color.Transparent
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.defaultScreenPadding),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(
                            all = MaterialTheme.spacing.small
                        )
                        .fillMaxWidth(),
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight(fontWeight)
                )
            }

            item {
                TimeDateCard(startTime, endTime, date,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large))
            }


            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier.padding(
                        all = MaterialTheme.spacing.small
                    )
                ){

                    aud?.let { aud ->
                        if (aud.isNotEmpty()){
                            SimpleShip(
                                label = aud,
                                isHighlight = isNow
                            )
                        }

                    }

                    type?.let { type ->
                        if (type.isNotEmpty()){
                            SimpleShip(
                                label = type,
                                isHighlight = isNow
                            )
                        }

                    }
                }
            }

            if (persons.isNotEmpty()){
                item {
                    Text(
                        text = "Преподаватели",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMediumEmphasized,
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.medium)
                            .padding(horizontal = MaterialTheme.rounding.extraLarge)
                    )
                }
                item {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier
                            .padding(bottom = MaterialTheme.spacing.defaultScreenPadding)
                            .clip(MaterialTheme.shapes.large)
                            .fillMaxWidth()
                    ) {
                        for (person in persons){
                            PersonItem(
                                name = person.name ?: "",
                                avatarUrl = person.avatarUrl,
                                onClick = { onPersonClick(person.personId) }
                            )
                        }
                    }
                }
            }



        }
    }
}

@Preview(showBackground = true, device = "spec:width=1080px,height=1080px,dpi=440")
@Composable
private fun LessonDetailScreenPreview() {
    AppTheme {
        LessonDetailScreen(
            name = "Высшая математика",
            type = "Лекция",
            aud = "V-402",
            startTime = LocalTime.now().minusHours(1),
            endTime = LocalTime.now().plusHours(1),
            date = LocalDate.now(),
            persons = listOf(
                PersonDetailDto(
                    personId = 1L,
                    name = "Иванов Иван Иванович",
                ),
                PersonDetailDto(
                    personId = 1L,
                    name = "Иванов Иван Иванович",
                ),
                PersonDetailDto(
                    personId = 1L,
                    name = "Иванов Иван Иванович",
                )
            )
        )
    }
}


