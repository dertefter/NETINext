package com.dertefter.data.datasource.local.room.dao

import androidx.room.*
import com.dertefter.data.datasource.local.room.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE login = :login")
    fun getAccount(login: String): Flow<AccountEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE login = :login")
    suspend fun deleteAccount(login: String)

    @Query("SELECT login FROM accounts")
    fun getLoginHistory(): Flow<List<String>>
}
