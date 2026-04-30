package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.docs.DocsItemDto
import com.dertefter.data.dto.docs.DocumentOptionItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseDocsList(
    html: String,
): List<DocsItemDto> {
    val doc: Document = Jsoup.parse(html)
    val rows = doc.select("div.row.as-table")

    return rows.map { row ->
        val cols = row.select("div[class^=col-]")

        DocsItemDto(
            type = cols.getOrNull(0)?.selectFirst("b")?.text() ?: "",
            date = cols.getOrNull(1)?.text()?.trim(),
            status = cols.getOrNull(2)?.text()?.trim(),
            person = cols.getOrNull(3)?.text()?.trim(),
            comment = cols.getOrNull(4)?.selectFirst("i")?.text()?.trim(),
            number = cols.getOrNull(5)?.ownText()?.trim()
                ?: cols.getOrNull(5)?.text()?.replace("№", "")?.trim()
        )
    }
}

fun parseDocsOptionsList(html: String): List<DocumentOptionItem> {
    val doc = Jsoup.parse(html)
    val output = mutableListOf<DocumentOptionItem>()

    val options = doc.select("select.types").first()?.select("option")
    if (options != null) {
        for (i in options){
            output.add(
                DocumentOptionItem(
                    text = i.text(),
                    value = i.attr("value")
                )
            )
        }
    }
    return output
}

fun parseDocCancelable(html: String): Boolean {
    val doc = Jsoup.parse(html)
    val exists = doc.select("button")
        .any { it.text().contains("Удалить заявку") }
    return exists
}
