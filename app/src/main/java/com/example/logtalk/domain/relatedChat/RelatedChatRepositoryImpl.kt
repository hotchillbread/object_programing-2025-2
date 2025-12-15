package com.example.logtalk.domain.relatedChat

import com.example.logtalk.data.local.TitleDao
import com.example.logtalk.domain.model.Session
import java.nio.ByteBuffer
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class RelatedChatRepositoryImpl @Inject constructor(
    private val titleDao: TitleDao
) : RelatedChatRepository {

    override suspend fun getSimilarSessions(sessionId: Long): List<Session> {
        val targetTitle = titleDao.getTitleById(sessionId) ?: return emptyList()
        val targetEmbedding = targetTitle.embedding?.let { toDoubleList(it) } ?: return emptyList()

        val allTitles = titleDao.getAllEmbeddingsExceptCurrent(sessionId)

        return allTitles
            .mapNotNull { titleData ->
                val embedding = titleData.embedding?.let { toDoubleList(it) } ?: return@mapNotNull null
                val similarity = cosineSimilarity(targetEmbedding, embedding)
                Pair(titleData, similarity)
            }
            .sortedByDescending { it.second }
            .take(5)
            .map {
                Session(
                    id = it.first.titleId,
                    title = it.first.title,
                    lastMessage = null,
                    createdAt = Instant.ofEpochMilli(it.first.createdAt),
                    updatedAt = null
                )
            }
    }

    private fun toDoubleList(bytes: ByteArray): List<Double> {
        val buffer = ByteBuffer.wrap(bytes)
        val doubleList = mutableListOf<Double>()
        while (buffer.hasRemaining()) {
            doubleList.add(buffer.double)
        }
        return doubleList
    }

    private fun cosineSimilarity(vec1: List<Double>, vec2: List<Double>): Double {
        val dotProduct = vec1.zip(vec2).sumOf { (a, b) -> a * b }
        val norm1 = sqrt(vec1.sumOf { it * it })
        val norm2 = sqrt(vec2.sumOf { it * it })
        return dotProduct / (norm1 * norm2)
    }
}