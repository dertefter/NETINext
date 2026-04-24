package com.dertefter.data.datasource.remote.api.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseShareScoreLink(html: String): String {
    val doc: Document = Jsoup.parse(html)

    val textArea = doc.getElementById("view_grades")

    return textArea?.text() ?: ""
}