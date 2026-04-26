package com.dertefter.data.datasource.local.room.dao

import androidx.room.*
import com.dertefter.data.datasource.local.room.entity.SessiaScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessiaScheduleDao {
    @Query("SELECT * FROM sessia_schedules WHERE login = :login AND groupName = :groupName")
    fun getSessiaSchedule(login: String, groupName: String): Flow<SessiaScheduleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessiaSchedule(schedule: SessiaScheduleEntity)

    @Query("DELETE FROM sessia_schedules WHERE login = :login AND groupName = :groupName")
    suspend fun deleteSessiaSchedule(login: String, groupName: String)
}
