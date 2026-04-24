package com.dertefter.data.repository

import com.dertefter.data.dto.messsages.MessageDto
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun updateMessages(): Result<List<MessageDto>>

    fun getMessagesFlow(): Flow<List<MessageDto>>

    fun getMessageByIdFlow(idMessage: Long): Flow<MessageDto?>

    suspend fun readMessage(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun unreadMessage(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun moveMessageToTrash(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun removeMessageFromTrash(idStudent: Long?, idMessage: Long): Result<Unit>

    suspend fun deleteMessageForever(idStudent: Long?, idMessage: Long): Result<Unit>


}