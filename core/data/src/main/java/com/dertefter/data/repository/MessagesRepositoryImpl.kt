package com.dertefter.data.repository

import com.dertefter.data.datasource.local.LocalDataSource
import com.dertefter.data.datasource.remote.RemoteDataSource
import com.dertefter.data.dto.messsages.MessageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : MessagesRepository {

    override suspend fun updateMessages(): Result<List<MessageDto>> {
        val messages = remoteDataSource.getMessages()
        return messages.onSuccess { messages ->
            localDataSource.saveMessages(messages)
        }
    }

    override fun getMessagesFlow(): Flow<List<MessageDto>> {
        return localDataSource.getMessages()
    }

    override fun getMessageByIdFlow(idMessage: Long): Flow<MessageDto?> {
        return localDataSource.getMessageById(idMessage)
    }

    override suspend fun readMessage(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        updateLocalMessageIsRead(idMessage, 1)
        return remoteDataSource.readMessage(idStudent, idMessage)
    }

    override suspend fun unreadMessage(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        updateLocalMessageIsRead(idMessage, 0)
        return  remoteDataSource.unreadMessage(idStudent, idMessage)
    }

    override suspend fun moveMessageToTrash(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        updateLocalMessageIsDeleted(idMessage, 1)
        return remoteDataSource.moveMessageToTrash(idStudent, idMessage)
            .onFailure {
                updateLocalMessageIsDeleted(idMessage, 0)
            }
    }

    override suspend fun removeMessageFromTrash(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        updateLocalMessageIsDeleted(idMessage, 0)
        return  remoteDataSource.removeMessageFromTrash(idStudent, idMessage).onFailure {
            updateLocalMessageIsDeleted(idMessage, 1)
        }
    }

    override suspend fun deleteMessageForever(
        idStudent: Long?,
        idMessage: Long
    ): Result<Unit> {
        val message = localDataSource.getMessageById(idMessage).first()
        localDataSource.deleteMessage(idMessage)
        return remoteDataSource.deleteMessageForever(idStudent, idMessage)
            .onFailure {
                message?.let { localDataSource.updateMessage(it) }
            }
    }

    private suspend fun updateLocalMessageIsRead(idMessage: Long, isRead: Int){
        localDataSource.getMessages().first().find { it.id == idMessage }?.let{ message ->
            localDataSource.updateMessage(message.copy(isRead = isRead))
        }
    }

    private suspend fun updateLocalMessageIsDeleted(idMessage: Long, isDeleted: Int){
        localDataSource.getMessages().first().find { it.id == idMessage }?.let{ message ->
            localDataSource.updateMessage(message.copy(isDeleted = isDeleted))
        }
    }



}