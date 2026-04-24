package com.dertefter.data.datasource.local.room.dao

import com.dertefter.data.datasource.remote.PreferredRemoteSource
import androidx.room.*
import com.dertefter.data.datasource.local.room.converter.Converters
import com.dertefter.data.datasource.local.room.entity.GlobalConfigEntity
import com.dertefter.data.dto.news.PromoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GlobalConfigDao {
    @Query("SELECT currentLogin FROM global_config WHERE id = 0")
    fun getCurrentLogin(): Flow<String?>

    @Query("SELECT preferredRemoteSource FROM global_config WHERE id = 0")
    fun getPreferredRemoteSource(): Flow<PreferredRemoteSource?>

    @Query("SELECT themeColor FROM global_config WHERE id = 0")
    fun getThemeColor(): Flow<Long?>

    @Query("SELECT isShapeCut FROM global_config WHERE id = 0")
    fun getIsShapeCut(): Flow<Boolean?>

    @Query("SELECT isNotificationEnabled FROM global_config WHERE id = 0")
    fun getIsNotificationEnabled(): Flow<Boolean?>

    @Query("SELECT * FROM global_config WHERE id = 0")
    fun getPromoList(): Flow<GlobalConfigEntity?>

    @Query("SELECT * FROM global_config WHERE id = 0")
    fun getConfig(): Flow<GlobalConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: GlobalConfigEntity)

    @Query("UPDATE global_config SET currentLogin = :login WHERE id = 0")
    suspend fun updateCurrentLogin(login: String?)

    @Query("UPDATE global_config SET preferredRemoteSource = :p WHERE id = 0")
    suspend fun updatePreferredRemoteSource(p: PreferredRemoteSource)

    @Query("UPDATE global_config SET themeColor = :color WHERE id = 0")
    suspend fun updateThemeColor(color: Long?)
}
