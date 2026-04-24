package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.money.MoneyItemDto
import com.dertefter.data.dto.schedule.GroupDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode


fun parseMoneyYearList(html: String): List<String> {
    val doc: Document = Jsoup.parse(html)
    val years = mutableListOf<String>()
    val select_year = doc.select("select#year").first()

    val option_list = select_year?.select("option")
    if (option_list != null) {
        for (option in option_list){
            val year = option.attr("value")
            years.add(year)
        }
    }

    return years
}


fun parseMoneyItems(html: String): List<MoneyItemDto> {
    val doc: Document = Jsoup.parse(html)
    val items = mutableListOf<MoneyItemDto>()

    val monthsList = listOf(
        "январь", "февраль", "март", "апрель", "май", "июнь",
        "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
    )

    doc.select("b").forEach { element ->
        val monthName = element.text().trim()
        if (monthName.lowercase() in monthsList) {
            var infoText = ""
            var nextNode = element.nextSibling()

            while (nextNode != null) {
                when (nextNode) {
                    is TextNode -> {
                        val text = (nextNode as TextNode).text().trim()
                        if (text.isNotEmpty()) {
                            infoText = text
                            break
                        }
                    }
                    is Element -> {
                        val el = nextNode as Element
                        if (el.tagName() == "div" && el.text().isNotBlank()) {
                            infoText = el.text().trim()
                            break
                        }
                    }
                }
                nextNode = nextNode.nextSibling()
            }

            items.add(MoneyItemDto(monthName, infoText))
        }
    }

    return items.reversed()
}