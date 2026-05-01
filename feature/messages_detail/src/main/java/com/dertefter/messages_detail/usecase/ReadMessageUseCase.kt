package com.dertefter.messages_detail.usecase

import com.dertefter.data.repository.MessagesRepository
import javax.inject.Inject

class ReadMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(idStudent: Long?, idMessage: Long): Result<Unit> {
        return messagesRepository.readMessage(idStudent, idMessage)
    }
}
