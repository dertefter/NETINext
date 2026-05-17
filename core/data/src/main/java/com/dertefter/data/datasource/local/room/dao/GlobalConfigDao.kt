package com.dertefter.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dertefter.data.datasource.local.room.entity.GlobalConfigEntity
import com.dertefter.data.datasource.remote.PreferredRemoteSource
import com.dertefter.data.dto.settings.ThemeStyle
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalConfigDao {

    @Query("SELECT preferredRemoteSource FROM global_config WHERE id = 0")
    fun getPreferredRemoteSource(): Flow<PreferredRemoteSource?>

    @Query("SELECT themeColor FROM global_config WHERE id = 0")
    fun getThemeColor(): Flow<Long?>

    @Query("SELECT themeStyle FROM global_config WHERE id = 0")
    fun getThemeStyle(): Flow<ThemeStyle?>

    @Query("SELECT newColorSpecVersion FROM global_config WHERE id = 0")
    fun getNewColorSpecVersion(): Flow<Boolean?>

    @Query("SELECT isShapeCut FROM global_config WHERE id = 0")
    fun getIsShapeCut(): Flow<Boolean?>

    @Query("SELECT isNotificationEnabled FROM global_config WHERE id = 0")
    fun getIsNotificationEnabled(): Flow<Boolean?>

    @Query("SELECT * FROM global_config WHERE id = 0")
    fun getPromoList(): Flow<GlobalConfigEntity?>

    @Query("SELECT * FROM global_config WHERE id = 0")
    fun getEvents(): Flow<GlobalConfigEntity?>

    @Query("SELECT weekHeader FROM global_config WHERE id = 0")
    fun getWeekHeader(): Flow<String?>

    @Query("SELECT isMessagesAlertSkipped FROM global_config WHERE id = 0")
    fun getIsMessagesAlertSkipped(): Flow<Boolean?>

    @Query("SELECT isTgLinkShow FROM global_config WHERE id = 0")
    fun getIsTgLinkShow(): Flow<Boolean?>

    @Query("SELECT * FROM global_config WHERE id = 0")
    fun getConfig(): Flow<GlobalConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: GlobalConfigEntity)

    @Query("UPDATE global_config SET preferredRemoteSource = :p WHERE id = 0")
    suspend fun updatePreferredRemoteSource(p: PreferredRemoteSource)

    @Query("UPDATE global_config SET themeColor = :color WHERE id = 0")
    suspend fun updateThemeColor(color: Long?)
}
