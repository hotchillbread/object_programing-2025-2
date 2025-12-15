package com.example.logtalk.domain.relatedChat

import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.relatedChat.RelatedChatRepository
import javax.inject.Inject

class GetSimilarSessionsUseCase @Inject constructor(
    private val repository: RelatedChatRepository
) {
    suspend operator fun invoke(sessionId: Long): List<Session> {
        return repository.getSimilarSessions(sessionId)
    }
}