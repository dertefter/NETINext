package com.dertefter.navigation

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
sealed interface Routes {


    @Serializable
    data object Tab1 : Routes

    @Serializable
    data object Tab2 : Routes

    @Serializable
    data object Tab3 : Routes

    @Serializable
    data object Tab4 : Routes

    @Serializable
    data object Profile : Routes

    @Serializable
    data object Auth : Routes


    @Serializable
    data object Money : Routes

    @Serializable
    data object ContactInfo : Routes

    @Serializable
    data object SearchGroup : Routes

    @Serializable
    data object Calendar : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Settings : Routes

    @Serializable
    data object SettingsAccount : Routes

    @Serializable
    data object SettingsAbout : Routes

    @Serializable
    data object SettingsLabs : Routes

    @Serializable
    data object SettingsTheme : Routes

    @Serializable
    data object SettingsNotifications : Routes

    @Serializable
    data object SwapLks : Routes

    @Serializable
    data object Docs : Routes

    @Serializable
    data object NewDocument : Routes

    @Serializable
    data class DocDetail(
        val type: String,
        val date: String?,
        val status: String?,
        val person: String?,
        val comment: String?,
        val number: String?,
    ) : Routes


    @Serializable
    data object Messages : Routes

    @Serializable
    data object ControlWeeks : Routes

    @Serializable
    data class MessagesDetail(
        val idMessage: Long,
        val idStudent: Long? = null,
    ) : Routes

    @Serializable
    data class NewsDetail(
        val newsId: String,
        val previewUrl: String? = null,
        val type: String? = null,
        val tags: String? = null,
        val date: String? = null,
        val contentColor: Long? = null,
        val link: String? = null
    ) : Routes

    @Serializable
    data object SearchPerson : Routes

    @Serializable
    data class PersonDetail(val personId: Long) : Routes

    @Serializable
    data class LessonDetail(
        val name: String,
        val type: String? = null,
        val aud: String? = null,
        val personIds: List<Long> = emptyList(),
        val startTimeString: String,
        val endTimeString: String,
        val dateString: String
    ) : Routes {
        fun getStartTime(): LocalTime {
            return LocalTime.parse(startTimeString)
        }

        fun getEndTime(): LocalTime {
            return LocalTime.parse(endTimeString)
        }

        fun getDate(): LocalDate {
            return LocalDate.parse(dateString)
        }
    }

    @Serializable
    data object SessiaResults : Routes

    @Serializable
    data object ShareScore : Routes

    @Serializable
    data class ImageViewer(
        val imageUrls: List<String>,
        val viewPosition: Int? = null,
    ) : Routes

}
