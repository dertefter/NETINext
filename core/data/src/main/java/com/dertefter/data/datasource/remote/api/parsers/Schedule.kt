package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.person.PersonShortDto
import com.dertefter.data.dto.schedule.LessonDto
import com.dertefter.data.dto.schedule.ScheduleDto
import com.dertefter.data.dto.schedule.TimeSlotDto
import com.dertefter.data.dto.schedule.WeekBoundsDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun parseSchedule(
    scheduleFirstWeekRespHtml: String,
    basicSchedulePrintResponseHtml: String,
    schedulePrintResponseHtml: String,
): ScheduleDto {
    val timeSlots = mutableListOf<TimeSlotDto>()
    val doc: Document = Jsoup.parse(basicSchedulePrintResponseHtml)
    val weeksCount = parseWeekCount(scheduleFirstWeekRespHtml)

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val firstDayDateString = parseFirstDayDateString(scheduleFirstWeekRespHtml)

    val monthValue = firstDayDateString.split(".")[1].toIntOrNull()
    val years = doc.select("span.schedule__title-content").first()?.text()?.split(" ")?.first()?.split("/")!!
    val year = if (monthValue in 9..12) years[0] else years[1]
    val combinedDate = "$firstDayDateString.$year"
    val firstDayLocalDate = LocalDate.parse(combinedDate, formatter)

    val doc2 = Jsoup.parse(schedulePrintResponseHtml)

    val schedule__table_body = doc2.select("div.schedule__table-body").first()

    for (weekNumber in 1..weeksCount){
        val weekIndex = weekNumber - 1
        val daysItems = schedule__table_body?.select("> *")!!
        for (dayIndex in 0..<daysItems.size){
            val dayItem = daysItems[dayIndex]
            val cell = dayItem.select("div.schedule__table-cell")[1]
            val timeItems = cell.select("> *")
            for (time in timeItems){
                val timeRange = time.select("div.schedule__table-time").text()
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val times = timeRange.split("-")
                if (times.size != 2) throw IllegalArgumentException("Неверный формат строки времени")
                val slotTimeStart = LocalTime.parse(times[0].trim(), formatter)
                val slotTimeEnd = LocalTime.parse(times[1].trim(), formatter)
                val slotDate = firstDayLocalDate.plusWeeks(weekIndex.toLong()).plusDays(dayIndex.toLong())
                val lessonsDtoList = mutableListOf<LessonDto>()

                val lessons = time.select("div.schedule__table-cell")[1].select("> *")
                for (lesson in lessons){

                    val label = lesson.select("span.schedule__table-label").first()?.text()

                    if (!label.isNullOrEmpty()) {
                        if (label.contains("по чётным") && !weekNumber.isEven()){
                            continue
                        } else if (label.contains("по нечётным") && !weekNumber.isOdd()){
                            continue
                        } else if (label.contains("недели")){
                            val triggerWeeks = mutableListOf<Int>()
                            val triggerWeeksString = label.split(" ")
                            for (i in 1..<triggerWeeksString.size){
                                triggerWeeks.add(triggerWeeksString[i].toInt())
                            }
                            if (!triggerWeeks.contains(weekNumber)){
                                continue
                            }
                        }
                    }

                    val lessonTitle = lesson.select("div.schedule__table-item").first()!!.ownText().replace("·", "").replace(",", "")
                    val aud = lesson.select("span.schedule__table-class").text()
                    val type = lesson.select("span.schedule__table-typework").text()
                    val person_links_list = lesson.select("a")
                    val persons = mutableListOf<PersonShortDto>()
                    for (p in person_links_list){
                        val link = p.attr("href")
                        val personId = link.substringAfterLast('/').toLongOrNull()
                        val name = p.ownText()
                        if (personId != null) {
                            persons.add(
                                PersonShortDto(
                                    personId = personId,
                                    name = name,
                                    avatarUrl = null
                                )
                            )
                        }
                    }
                    if (lessonTitle.isNotEmpty()){
                        val lessonDto = LessonDto(
                            name = lessonTitle,
                            type = type,
                            aud = aud,
                            persons = persons
                        )
                        lessonsDtoList.add(lessonDto)
                    }
                }
                if (lessonsDtoList.isNotEmpty()) {
                    timeSlots.add(
                        TimeSlotDto(
                            dateString = slotDate.toString(),
                            startTimeString = slotTimeStart.toString(),
                            endTimeString = slotTimeEnd.toString(),
                            lessons = lessonsDtoList
                        )
                    )
                }
            }
        }

    }

    val weekBounds = createWeekBounds(firstDayLocalDate, weeksCount)

    return ScheduleDto(timeSlots, weekBounds)

}

fun parseSessiaSchedule(html: String): List<TimeSlotDto> {


    fun formatDate(input: String): String {
        val formatterIn = DateTimeFormatter.ofPattern("dd.MM.yy")
        val formatterOut = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(input, formatterIn).format(formatterOut)
    }

    val result = mutableListOf<TimeSlotDto>()
    val doc = Jsoup.parse(html)

    val rows = doc.select(".schedule__session-body > .schedule__session-row")

    for (row in rows) {
        val date = row.selectFirst(".schedule__session-day")?.text()?.trim()
            ?: continue

        // время (может отсутствовать)
        val timeText = row.selectFirst(".schedule__session-time")?.text()?.trim()
        val startTime = if (!timeText.isNullOrBlank()) {
            LocalTime.parse(timeText)
        } else {
            LocalTime.of(8, 30)
        }

        val endTime = startTime.plusHours(1).plusMinutes(30)

        // предмет
        val itemBlock = row.selectFirst(".schedule__session-item")
        val name = itemBlock?.ownText()?.trim().orEmpty()

        // преподаватели
        val persons = itemBlock
            ?.select("a")
            ?.map {
                PersonShortDto(
                    personId = it.attr("href")
                        .substringAfterLast("/")
                        .toLongOrNull() ?: 0L,
                    name = it.text(),
                    avatarUrl = null
                )
            }

        // аудитория
        val aud = row.selectFirst(".schedule__session-class")?.text()?.trim()

        // тип (экзамен / консультация)
        val type = row.selectFirst(".schedule__session-label")?.text()?.trim()

        val lesson = LessonDto(
            name = name,
            type = type,
            aud = aud,
            persons = persons
        )

        result.add(
            TimeSlotDto(
                dateString = formatDate(date),
                startTimeString = startTime.toString(),
                endTimeString = endTime.toString(),
                lessons = listOf(lesson)
            )
        )
    }

    return result
}


fun parseFirstDayDateString(html: String): String {
    val doc: Document = Jsoup.parse(html)
    val firstDayDateString = doc.select("span.schedule__table-date").first()?.text()
    return firstDayDateString!!
}

fun parseWeekHeader(html: String): String {
    val doc: Document = Jsoup.parse(html)
    return doc.select("div.header__label").first()?.text()!!
}


fun parseWeekCount(html: String): Int {
    val doc: Document = Jsoup.parse(html)
    val weeksContainer = doc.select("div.schedule__weeks").first()
    val links = weeksContainer?.select("a.schedule__weeks-item")!!
    val count = links.size
    return count
}


fun createWeekBounds(firstDayDate: LocalDate, weekCount: Int): List<WeekBoundsDto>{
    val weekBoundsList = mutableListOf<WeekBoundsDto>()
    for (weekNumber in 0..<weekCount){
        val weekBoundsDto = WeekBoundsDto(
            startDateString = (firstDayDate.plusWeeks(weekNumber.toLong())).toString(),
            weekNumber = weekNumber + 1
        )
        weekBoundsList.add(weekBoundsDto)
    }
    return weekBoundsList.toList()
}




fun Int.isEven(): Boolean {
    return this % 2 == 0
}

fun Int.isOdd(): Boolean {
    return this % 2 != 0
}