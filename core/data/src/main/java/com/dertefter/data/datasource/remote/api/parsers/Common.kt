package com.dertefter.data.datasource.remote.api.parsers

import com.dertefter.data.dto.auth.Login2FormParams
import com.dertefter.data.dto.user.ContactInfoDto
import com.dertefter.data.dto.user.LksDto
import org.jsoup.Jsoup

fun parseFormParams(html: String): Login2FormParams {
    val actionUrl = Jsoup.parse(html).selectFirst("form.login-form")?.attr("action")
    val params = actionUrl?.substringAfter("?", "")
        ?.split("&")
        ?.associate {
            val pair = it.split("=", limit = 2)
            pair[0] to pair.getOrElse(1) { "" }
        }.orEmpty()

    if (params.isEmpty()) throw NullPointerException()
    return Login2FormParams(
        sessionCode = params["session_code"].orEmpty(),
        execution = params["execution"].orEmpty(),
        clientId = params["client_id"].orEmpty(),
        tabId = params["tab_id"].orEmpty(),
        clientData = params["client_data"].orEmpty(),
    )
}



fun verifyAuth(html: String): Boolean {
    val loginContainer = Jsoup.parse(html).selectFirst("div.login-container")
    return loginContainer == null

}



fun parseContactInfo(html: String): ContactInfoDto {
    val doc = Jsoup.parse(html)
    val fullName = doc.select("span.fio").text().split(",")[0].split(" ")
    val name = fullName.getOrNull(1)
    val surname = fullName.getOrNull(0)
    val patronymic = fullName.getOrNull(2)
    val symGroup = doc.select("span.fio").text().split(",")[1].replace(" ", "")
    val email = doc.selectFirst("input[name=n_email]")?.attr("value")
    val address = doc.selectFirst("input[name=n_address]")?.attr("value")
    val mobilePhoneNumber = doc.selectFirst("input[name=n_phone]")?.attr("value")
    val snils = doc.selectFirst("input[name=n_snils]")?.attr("value")
    val oms = doc.selectFirst("input[name=n_oms]")?.attr("value")
    val vk = doc.selectFirst("input[name=n_vk]")?.attr("value")
    val tg = doc.selectFirst("input[name=n_tg]")?.attr("value")
    val leaderId = doc.selectFirst("input[name=n_leader]")?.attr("value")

    val lksList = mutableListOf<LksDto>()
    val elements = doc.select("div#other_lks_content > div.other_lks")

    for (element in elements.orEmpty()) {
        val id = element.select("a[onclick]").first()?.let { a ->
            val onclick = a.attr("onclick")
            Regex("selectOtherLks\\((\\d+)\\)").find(onclick)?.groupValues?.get(1)?.toInt()
        }
        val title = element.select("b").first()?.text()?.trim() ?: continue
        val subtitle =
            element.ownText().takeIf { it.isNotBlank() }?.trim()?.removeSurrounding("(", ")")
                ?.trim()
        val isSelected = element.select("span:contains(выбран)").isNotEmpty() || id == null

        lksList.add(LksDto(title, subtitle, id, isSelected))
    }

    return ContactInfoDto(
        name = name,
        surname = surname,
        patronymic = patronymic,
        symGroup = symGroup,
        email = email,
        address = address,
        mobilePhoneNumber = mobilePhoneNumber,
        snils = snils,
        oms = oms,
        vk = vk,
        telegram = tg,
        leaderId = leaderId
    )


}

fun parseLksList(html: String): List<LksDto> {
    val doc = Jsoup.parse(html)
    val lksList = mutableListOf<LksDto>()
    val elements = doc.select("div#other_lks_content > div.other_lks")

    for (element in elements.orEmpty()) {
        val id = element.select("a[onclick]").first()?.let { a ->
            val onclick = a.attr("onclick")
            Regex("selectOtherLks\\((\\d+)\\)").find(onclick)?.groupValues?.get(1)?.toInt()
        }
        val title = element.select("b").first()?.text()?.trim() ?: continue
        val subtitle =
            element.ownText().takeIf { it.isNotBlank() }?.trim()?.removeSurrounding("(", ")")
                ?.trim()
        val isSelected = element.select("span:contains(выбран)").isNotEmpty() || id == null

        lksList.add(LksDto(title, subtitle, id, isSelected))
    }
    return lksList
}

