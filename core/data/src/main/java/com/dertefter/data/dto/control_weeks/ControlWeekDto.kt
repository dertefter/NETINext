package com.dertefter.data.dto.control_weeks

import com.google.gson.annotations.SerializedName

data class ControlWeekDto(

    @SerializedName("CONTROL_WEEK_TITLE")
    val title: String,

    @SerializedName("CONTROL_WEEK_SEM")
    val semester: String,

    @SerializedName("CONTROL_WEEK_NUMBER")
    val week: String,

    @SerializedName("CONTROL_WEEK_VALUE")
    val value: String
)