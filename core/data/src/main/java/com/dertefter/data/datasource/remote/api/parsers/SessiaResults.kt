package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.sessia_results.SessiaResultDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseSessiaResults(html: String): List<SessiaResultDto> {
    val doc: Document = Jsoup.parse(html)
    val results = mutableListOf<SessiaResultDto>()

    val semesterHeaders = doc.select("h3:contains(Семестр)")

    for (header in semesterHeaders) {
        val semesterNumber = header.text().filter { it.isDigit() }.toIntOrNull() ?: continue

        val table = header.nextElementSibling()?.selectFirst("table") ?: continue

        val rows = table.select("tr.all_progress")

        for (row in rows) {
            val cols = row.select("td")
            if (cols.size < 6) continue
            val disciplineFullText = cols[1].text()
            val name = disciplineFullText.substringBeforeLast("(").trim()
            val typeName = disciplineFullText.substringAfterLast("(", "").substringBeforeLast(")").trim()

            val scoreStr = cols[3].select("span").text().trim()
            val score = scoreStr.toIntOrNull()

            val markName = cols[4].select("span").text().trim()
            val europeanMarkString = cols[5].select("span").text().trim().ifEmpty { null }

            results.add(
                SessiaResultDto(
                    europeanMarkString = europeanMarkString,
                    markName = markName,
                    name = name,
                    score = score,
                    semester = semesterNumber,
                    typeName = typeName
                )
            )
        }
    }

    return results
}