package com.dertefter.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dertefter.data.datasource.local.room.entity.MoneyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneyDao {
    @Query("SELECT * FROM money WHERE login = :login AND year = :year")
    fun getMoney(login: String, year: String): Flow<MoneyEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoney(money: MoneyEntity)
}
