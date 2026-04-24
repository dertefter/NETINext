package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.messsages.MessageDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseMessages(
    undeletedHtml: String,
    deletedHtml: String
): List<MessageDto> {

    val result = mutableListOf<MessageDto>()

    result += parseFromDocument(
        doc = Jsoup.parse(undeletedHtml),
        selectors = listOf(
            "div#tabs1-messages",
            "div#tabs2-messages"
        ),
        isDeleted = 0
    )

    result += parseFromDocument(
        doc = Jsoup.parse(deletedHtml),
        selectors = listOf("div.notab_content"),
        isDeleted = 1
    )

    return result
}

private fun parseFromDocument(
    doc: Document,
    selectors: List<String>,
    isDeleted: Int
): List<MessageDto> {

    val out = mutableListOf<MessageDto>()

    for (selector in selectors) {
        val container = doc.select(selector).first() ?: continue
        val pads = container.select("div.pad")

        val senderType = if (selector == "div#tabs1-messages") 101 else 102

        for (pad in pads) {
            val col2 = pad.select("div.col-2").first() ?: continue
            val col8 = pad.select("div.col-8").first() ?: continue

            val onclick = col2.attr("onclick")
            val messageId = Regex("id=(\\d+)")
                .find(onclick)
                ?.groupValues
                ?.get(1)
                ?.toLongOrNull() ?: 0L

            val fioAuthor = col2.select("nobr").text().trim()

            val dateString = pad.select("div.col-1")
                .last()
                ?.text()
                ?.trim()

            val title = col8.select("span")
                .first()
                ?.text()
                ?.trim()
                ?: ""

            val text = col8.select("span.message_text")
                .text()
                .replaceFirst(Regex("^--\\s*"), "")
                .trim()

            val isRead = if (
                pad.select("div.new_message_header").isNotEmpty() ||
                pad.hasClass("not_read")
            ) 0 else 1

            out.add(
                MessageDto(
                    id = messageId,
                    fioAuthor = fioAuthor,
                    dateSent = dateString,
                    text = text,
                    title = title,
                    isRead = isRead,
                    isDeleted = isDeleted,
                    senderType = senderType
                )
            )
        }
    }

    return out
}
