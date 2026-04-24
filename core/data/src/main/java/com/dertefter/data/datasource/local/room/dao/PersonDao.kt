package com.dertefter.data.datasource.local.room.dao

import androidx.room.*
import com.dertefter.data.datasource.local.room.entity.PersonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons WHERE personId = :personId")
    fun getPersonById(personId: Long): Flow<PersonEntity?>

    @Query("SELECT * FROM persons WHERE personId IN (:personIds)")
    fun getPersonsByIds(personIds: List<Long>): Flow<List<PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Query("DELETE FROM persons WHERE personId = :personId")
    suspend fun deletePerson(personId: Long)
}
