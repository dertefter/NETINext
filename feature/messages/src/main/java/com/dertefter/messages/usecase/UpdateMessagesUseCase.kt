package com.dertefter.messages.usecase

import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.repository.MessagesRepository
import javax.inject.Inject

class UpdateMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(): Result<List<MessageDto>> {
        return messagesRepository.updateMessages()
    }
}
