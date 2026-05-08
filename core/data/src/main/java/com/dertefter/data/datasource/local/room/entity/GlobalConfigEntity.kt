package com.dertefter.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.dto.news.PromoItem
import com.dertefter.data.dto.schedule.EventDto

@Entity(tableName = "global_config")
data class GlobalConfigEntity(
    @PrimaryKey val id: Int = 0,
    val currentLogin: String? = null,
    val preferredRemoteSource: PreferredRemoteSource = PreferredRemoteSource.AUTO,
    val themeColor: Long? = null,
    val isShapeCut: Boolean? = null,
    val isNotificationEnabled: Boolean? = null,
    val promoList: List<PromoItem>? = null,
    val eventList: List<EventDto>? = null,
    val weekHeader: String? = null,
    val isMessagesAlertSkipped: Boolean? = null,
    val isTgLinkShow: Boolean? = null
)
