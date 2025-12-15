package com.example.logtalk.data.repository

import com.example.logtalk.core.utils.model.OpenAIEmbeddingService
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.ByteBuffer
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val titleDao: TitleDao,
    private val messageDao: MessageDao, // Not used yet
    private val embeddingService: OpenAIEmbeddingService
) : SessionRepository {

    override suspend fun getRecentSessions(): List<Session> {
        // This should be properly implemented with the DAO
        return emptyList()
    }

    override suspend fun searchSessions(query: String): List<Session> {
        // This should be properly implemented with the DAO
        return emptyList()
    }

    override suspend fun getSessionById(id: Long): Session? {
        // This should be properly implemented with the DAO
        return null
    }

    override suspend fun createSession(): Long {
        val title = "새 세션"
        val embedding = embeddingService.createEmbeddings(listOf(title)).firstOrNull()
        val embeddingBytes = embedding?.let { doubleList ->
            val byteBuffer = ByteBuffer.allocate(doubleList.size * 8)
            doubleList.forEach { byteBuffer.putDouble(it) }
            byteBuffer.array()
        }

        val newTitle = com.example.logtalk.data.local.TitleData(
            title = title,
            embedding = embeddingBytes
        )
        return titleDao.insertTitle(newTitle)
    }

    override suspend fun deleteSession(id: Long) {
        // This should be properly implemented with the DAO
    }

    override fun observeSessions(): Flow<List<Session>> {
        return titleDao.getAllTitles().map {
            it.map {
                Session(
                    id = it.titleId,
                    title = it.title,
                    lastMessage = null, // This should be fetched from MessageDao
                    createdAt = Instant.ofEpochMilli(it.createdAt),
                    updatedAt = null // This should be updated
                )
            }
        }
    }
}
