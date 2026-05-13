package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.news.NewsDetailDto
import com.dertefter.data.dto.news.NewsItem
import com.dertefter.data.dto.news.PromoItem
import okhttp3.ResponseBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun parseNews(body: ResponseBody?): List<NewsItem> {
    val json = body?.use { it.string() }
    val jsonObject = JSONObject(json!!)

    val itemsHtml = jsonObject.optString("items")
    if (itemsHtml.isNullOrEmpty()) return emptyList()

    val doc = Jsoup.parse(itemsHtml)
    val newsElements = doc.select("a")

    return newsElements.mapNotNull { element ->
        val dataType = element.attr("data-type")
        if (dataType == "video") return@mapNotNull null

        val link = element.attr("href")
        val fullUrl = if (link.startsWith("http")) link else "https://www.nstu.ru$link"
        val id = link.substringAfter("idnews=", "").substringBefore("&")
        if (id.isEmpty()) return@mapNotNull null

        val style = element.attr("style")
        val imageUrl = if (style.contains("background-image: url(")) {
            val urlPath = style.substringAfter("url(")
                .substringBefore(")")
                .trim('\'', '"', ' ', ';')
                .removePrefix("//")
                .removePrefix("/")
            if (urlPath.isNotEmpty()) "https://www.nstu.ru/$urlPath" else null
        } else null

        NewsItem(
            id = id,
            type = element.select("div.main-events__item-type").text(),
            title = element.select("div.main-events__item-title").text(),
            tags = element.select("div.main-events__item-tags").text(),
            date = element.select("div.main-events__item-date").text(),
            imageUrl = imageUrl,
            detailUrl = fullUrl
        )
    }
}

fun parseNewsDetail(html: String): NewsDetailDto {
    val doc: Document = Jsoup.parse(html)

    val title = doc.select("div.page-title").text()

    val news_detail = doc.select("div.news-detail").first()
    val contentElements = news_detail?.select("div.col-9")
    contentElements?.select("img")?.remove()
    val contentHtml = contentElements?.html()
    val fotorama = doc.select("div.fotorama")
    val baseUrl = "https://www.nstu.ru"
    val imageUrls = mutableListOf<String>()

    val images = fotorama.select("img")
    for (i in images) {
        val imageUrl = baseUrl + i.attr("src")
        imageUrls.add(imageUrl)
    }

    return NewsDetailDto(title, contentHtml, imageUrls.toList())
}


fun parsePromo(html: String): List<PromoItem> {

    fun extractImageUrl(style: String): String {
        val regex = Regex("""background-image:\s*url\(['"]?(.*?)['"]?\)""")
        val match = regex.find(style) ?: return ""
        val urlPart = match.groupValues[1]
        return Jsoup.parse(urlPart).text().replace("[\"']".toRegex(), "")
    }


    val doc = Jsoup.parse(html)

    val mainPromo = doc.selectFirst("div.main-promo")
    val promoElements = mainPromo?.select("a")
        ?: return emptyList()

    val output = mutableListOf<PromoItem>()
    for (element in promoElements) {
        var link = element.attr("href")
        if (link[0] == '/') {
            link = "https://nstu.ru$link"
        }
        val style = element.attr("style")
        var imageUrl = extractImageUrl(style)
        imageUrl = "https://nstu.ru/$imageUrl"
        val title = element.selectFirst(".main-promo__slide-title")?.text()?.trim() ?: ""
        val subtitle = element.selectFirst(".main-promo__slide-subtitle")?.text()?.trim() ?: ""
        val desc = element.selectFirst(".main-promo__slide-desc")?.text()?.trim() ?: ""
        output.add(
            PromoItem(
                title,
                imageUrl,
                link,
                subtitle,
                desc
            )
        )
    }
    return output
}