package com.dertefter.data.datasource.local.room.dao

import androidx.room.*
import com.dertefter.data.datasource.local.room.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE login = :login AND groupName = :groupName")
    fun getSchedule(login: String, groupName: String): Flow<ScheduleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Query("DELETE FROM schedules WHERE login = :login AND groupName = :groupName")
    suspend fun deleteSchedule(login: String, groupName: String)
}
