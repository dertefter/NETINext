package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.control_weeks.ControlWeekDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseControlWeeks(html: String): List<ControlWeekDto> {
    val doc: Document = Jsoup.parse(html)
    val results = mutableListOf<ControlWeekDto>()
    val rows = doc.select("table.tdall tr")

    for (row in rows) {
        val cells = row.select("td")
        if (cells.size >= 5) {
            val title = cells[1].text().trim()
            val semester = cells[2].text().trim()
            val week = cells[3].text().trim()
            val value = cells[4].text().trim()

            if (value.isNotEmpty() && week.isNotEmpty() && semester.isNotEmpty() && title.isNotEmpty()){
                results.add(
                    ControlWeekDto(
                        title = title,
                        semester = semester,
                        week = week,
                        value = value
                    )
                )
            }
        }
    }

    return results
}