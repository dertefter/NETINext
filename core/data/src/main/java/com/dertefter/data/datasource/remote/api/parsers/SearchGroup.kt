package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.schedule.GroupDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


fun parseSearchGroupResults(html: String): List<GroupDto> {
    val doc: Document = Jsoup.parse(html)
    val elements = doc.select("a.schedule__search-result__item")
    return elements.map { element ->
        val groupName = element.text().trim()
        GroupDto(name = groupName, isIndividual = false)
    }
}