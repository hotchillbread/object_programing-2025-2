package com.example.logtalk.data.repository

import com.example.logtalk.core.utils.model.OpenAIEmbeddingService
import com.example.logtalk.data.local.MessageDao
import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.domain.model.Session
import com.example.logtalk.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.ByteBuffer
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    // TODO: SessionDao 주입 필요
    private val titleDao: TitleDao,
    private val messageDao: MessageDao, // Not used yet
    private val embeddingService: OpenAIEmbeddingService
) : SessionRepository {

    // 임시 메모리 저장소
    private val sessions = mutableListOf<Session>()
    private var nextId = 1L

    override suspend fun getRecentSessions(): List<Session> {
        // This should be properly implemented with the DAO
        return emptyList()
        // 임시: 샘플 데이터 생성
        if (sessions.isEmpty()) {
            sessions.addAll(
                listOf(
                    Session(
                        id = 1,
                        title = "세션 1",
                        lastMessage = "안녕하세요! 첫 번째 메시지입니다.",
                        createdAt = Instant.now(),
                        updatedAt = Instant.now()
                    ),
                    Session(
                        id = 2,
                        title = "세션 2",
                        lastMessage = "두 번째 세션의 마지막 메시지",
                        createdAt = Instant.now(),
                        updatedAt = Instant.now()
                    )
                )
            )
            nextId = 3
        }
        return sessions.sortedByDescending { it.updatedAt }
    }

    override suspend fun searchSessions(query: String): List<Session> {
        // This should be properly implemented with the DAO
        return emptyList()
        return sessions.filter {
            it.title.contains(query, ignoreCase = true) ||
                    (it.lastMessage?.contains(query, ignoreCase = true) == true)
        }.sortedByDescending { it.updatedAt }
    }

    override suspend fun getSessionById(id: Long): Session? {
        // This should be properly implemented with the DAO
        return null
        return sessions.find { it.id == id }
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
        val now = Instant.now()
        val newSession = Session(
            id = nextId++,
            title = "새 세션 ${nextId - 1}",
            lastMessage = null,
            createdAt = now,
            updatedAt = now
        )
        sessions.add(newSession)
        return newSession.id
    }

    override suspend fun deleteSession(id: Long) {
        sessions.removeAll { it.id == id }
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

