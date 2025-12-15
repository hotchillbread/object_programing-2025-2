package com.example.logtalk.domain.relatedChat

import com.example.logtalk.domain.model.Session


interface RelatedChatRepository {

    suspend fun getSimilarSessions(sessionId: Long): List<Session>
}