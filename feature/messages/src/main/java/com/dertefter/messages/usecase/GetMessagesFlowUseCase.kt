package com.dertefter.messages.usecase

import com.dertefter.data.dto.messsages.MessageDto
import com.dertefter.data.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesFlowUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(): Flow<List<MessageDto>> {
        return messagesRepository.getMessagesFlow()
    }
}
