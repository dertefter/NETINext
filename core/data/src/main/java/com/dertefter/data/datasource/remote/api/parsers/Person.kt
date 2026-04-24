package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.person.PersonDetailDto
import com.dertefter.data.dto.person.PersonShortDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document



fun parsePersonDetail(html: String, personIdFromUrl: Long): PersonDetailDto {
    val doc: Document = Jsoup.parse(html)

    val name = doc.selectFirst(".page-title h1")?.text()
    val avatarUrl = doc.selectFirst(".contacts__card-image img")?.attr("src")?.let {
        "https://ciu.nstu.ru/kaf/$it"
    }
    val post = doc.selectFirst(".contacts__card-post")?.text()

    val contactInfo = mutableSetOf<String>()
    doc.select(".contacts__card-email a").forEach { element ->
        val part1 = element.attr("data-email-1")
        val part2 = element.attr("data-email-2")
        if (part1.isNotEmpty() && part2.isNotEmpty()) {
            contactInfo.add("$part1@$part2")
        }
    }
    doc.select(".contacts__card-phone, .contacts__card-address, .contacts__card-time").forEach {
        val text = it.text().trim()
        if (text.isNotEmpty()) contactInfo.add(text)
    }

    val profiles = mutableListOf<String>()
    val profilesSection = doc.select("h3:contains(Профили)").first()?.parent()
    profilesSection?.let { section ->
        section.html().split(Regex("(?i)<br\\s*/?>")).forEach { line ->
            val fragment = Jsoup.parseBodyFragment(line).body()
            val textContent = fragment.text().trim()

            if (textContent.isNotEmpty() && !textContent.contains("Профили")) {
                profiles.add(line.trim())
            }
        }
    }

    val disciplines = doc.selectFirst(".about_disc")?.let { element ->
        element.html()
            .split(Regex("(?i)<br\\s*/?>"))
            .map { Jsoup.parse(it).text().trim() }
            .filter { it.isNotEmpty() && !it.contains("Преподаваемые дисциплины") }
    } ?: emptyList()

    val hasTimetable = html.contains("расписание занятий", ignoreCase = true)

    return PersonDetailDto(
        personId = personIdFromUrl,
        name = name,
        avatarUrl = avatarUrl,
        post = post,
        contactInfo = contactInfo.toList(),
        profiles = profiles, // Теперь здесь HTML-строки со ссылками
        disciplines = disciplines,
        hasTimetable = hasTimetable
    )
}

fun parsePersonShorts(html: String): List<PersonShortDto> {
    val doc = Jsoup.parse(html)
    val elements = doc.select("div.search-result__item")
    val output = mutableListOf<PersonShortDto>()
    for (element in elements) {
        val name = element.selectFirst(".search-result__title span")?.text() ?: ""
        var avatarUrl = element.selectFirst("img")?.attr("src")
        if (avatarUrl != null) {avatarUrl = "https://nstu.ru$avatarUrl"}
        val personalSiteUrl = element.selectFirst("a[title='На персональный сайт']")?.attr("href")
        val personId = personalSiteUrl?.trimEnd('/')?.substringAfterLast('/')?.toLongOrNull()

        if (personId != null) {
            output.add(PersonShortDto(
                personId = personId,
                name = name,
                avatarUrl = avatarUrl
            ))
        }
    }
    return output
}