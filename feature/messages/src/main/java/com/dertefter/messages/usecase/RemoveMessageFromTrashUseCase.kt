package com.dertefter.messages.usecase

import com.dertefter.data.repository.MessagesRepository
import javax.inject.Inject

class RemoveMessageFromTrashUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(idStudent: Long?, idMessage: Long): Result<Unit> {
        return messagesRepository.removeMessageFromTrash(idStudent, idMessage)
    }
}
