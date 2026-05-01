package com.dertefter.messages_detail.usecase

import com.dertefter.data.repository.MessagesRepository
import javax.inject.Inject

class SetMessageArchivedUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(idStudent: Long?, idMessage: Long, isArchived: Boolean): Result<Unit> {
        return if (isArchived) {
            messagesRepository.moveMessageToTrash(idStudent, idMessage)
        } else {
            messagesRepository.removeMessageFromTrash(idStudent, idMessage)
        }
    }
}
