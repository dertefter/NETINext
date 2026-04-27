package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.schedule.EventDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseEvents(
    html: String,
): List<EventDto> {
    val doc: Document = Jsoup.parse(html)
    val events = mutableListOf<EventDto>()

    val cells = doc.select("td.td_all")

    for (cell in cells) {
        val dayLink = cell.selectFirst("a[onclick^=selectDay]") ?: continue

        val onclickAttr = dayLink.attr("onclick")

        val regex = Regex("""selectDay\((\d+),\s*(\d+),\s*(\d+)\)""")
        val matchResult = regex.find(onclickAttr) ?: continue

        val (day, month, year) = matchResult.destructured
        val dateString = "${day.padStart(2, '0')}.${month.padStart(2, '0')}.$year"

        val eventLinks = cell.select("a[href^=/news]")

        for (link in eventLinks) {
            val title = link.text().trim()
            val url = link.attr("href")

            if (title.isNotEmpty()) {
                events.add(
                    EventDto(
                        title = title,
                        url = "https://nstu.ru$url",
                        dateString = dateString
                    )
                )
            }
        }
    }

    return events
}