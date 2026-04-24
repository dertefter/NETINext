package com.dertefter.data.dto.user

data class ContactInfoSaveDto(
    val save: String = "1",
    val what: String = "0",
    val save_oms: String = "",
    val n_email: String,
    val n_address: String,
    val n_phone: String,
    val n_snils: String,
    val n_oms: String,
    val n_vk: String,
    val n_tg: String,
    val n_leader: String,
    val n_has_agree: String = ""
)