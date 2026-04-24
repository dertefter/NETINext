package com.dertefter.contact_info.domain

import com.dertefter.data.dto.user.ContactInfoDto

data class ContactInfo(
    val email: String,
    val mobilePhoneNumber: String,
    val address: String,
    val snils: String,
    val oms: String,
    val vk: String,
    val telegram: String,
    val leaderId: String
)

fun ContactInfoDto.toContactInfo(): ContactInfo{
    return ContactInfo(
        email = this.email.orEmpty(),
        mobilePhoneNumber = this.mobilePhoneNumber.orEmpty().formatAsCiuPhoneNumber(),
        address = this.address.orEmpty(),
        snils = this.snils.orEmpty(),
        oms = this.oms.orEmpty(),
        vk = this.vk.orEmpty(),
        telegram = this.telegram.orEmpty(),
        leaderId = this.leaderId.orEmpty()
    )
}


/*
Насильно приводит к русскому формату номера,
ибо в редакторе контактной информации другого не дано :_)
 */

fun String.formatAsCiuPhoneNumber(): String {
    if (this.isBlank()) return ""

    val digits = this.filter { it.isDigit() }

    if (digits.isEmpty()) return ""

    val normalized = when {
        digits.startsWith("8") -> digits.drop(1)
        digits.startsWith("7") -> digits.drop(1)
        else -> digits
    }.take(10)

    val builder = StringBuilder("+7")

    if (normalized.isNotEmpty()) {
        builder.append(" (")
        builder.append(normalized.take(3))
    }

    if (normalized.length >= 3) {
        builder.append(")")
    }

    if (normalized.length > 3) {
        builder.append(" ")
        builder.append(normalized.substring(3, minOf(6, normalized.length)))
    }

    if (normalized.length >= 6) {
        builder.append("-")
        builder.append(normalized.substring(6, minOf(10, normalized.length)))
    }

    return builder.toString()
}