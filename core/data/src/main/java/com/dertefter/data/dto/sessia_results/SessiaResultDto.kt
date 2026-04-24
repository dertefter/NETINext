package com.dertefter.data.dto.sessia_results



import com.google.gson.annotations.SerializedName

data class SessiaResultDto(

    @SerializedName("CONFIRM_DATE")
    val confirmDate: String? = null,

    @SerializedName("CREDITS")
    val credits: Int? = null,

    @SerializedName("DISMISS_DATE")
    val dismissDate: String? = null,

    @SerializedName("EUROPEAN_MARK_NUMBER")
    val europeanMarkNumber: Int? = null,

    @SerializedName("EUROPEAN_MARK_STRING")
    val europeanMarkString: String? = null,

    @SerializedName("EXAM_DATE")
    val examDate: String? = null,

    @SerializedName("FIO_PRACT")
    val fioPract: String? = null,

    @SerializedName("HOURS")
    val hours: Int? = null,

    @SerializedName("IS_ZACH_DIFF")
    val isZachDiff: Int? = null,

    @SerializedName("KP_THEME")
    val kpTheme: String? = null,

    @SerializedName("MARK")
    val mark: Int? = null,

    @SerializedName("MARK_NAME")
    val markName: String? = null,

    @SerializedName("NAME")
    val name: String? = null,

    @SerializedName("PLACE_PRACT")
    val placePract: String? = null,

    @SerializedName("SCORE")
    val score: Int? = null,

    @SerializedName("SEMESTER")
    val semester: Int,

    @SerializedName("TYPE_NAME")
    val typeName: String? = null
)