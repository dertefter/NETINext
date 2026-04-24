package com.dertefter.data.datasource.local.room.dao

import androidx.room.*
import com.dertefter.data.datasource.local.room.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE userLogin = :login")
    fun getMessages(login: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE userLogin = :login AND id = :id")
    fun getMessageById(login: String, id: Long): Flow<MessageEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE userLogin = :login")
    suspend fun deleteMessages(login: String)

    @Query("DELETE FROM messages WHERE userLogin = :login AND id = :id")
    suspend fun deleteMessage(login: String, id: Long)
}
