package com.dertefter.messages_detail.usecase

import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(id: Long): Flow<MessageDto?> {
        return messagesRepository.getMessageByIdFlow(id)
    }
}
