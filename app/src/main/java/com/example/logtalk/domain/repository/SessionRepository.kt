package com.example.logtalk.domain.repository

import com.example.logtalk.domain.model.Session
import kotlinx.coroutines.flow.Flow

/**
 * 세션 데이터 접근을 위한 Repository 인터페이스
 */
interface SessionRepository {

    /**
     * 모든 세션을 최신순으로 조회
     */
    suspend fun getRecentSessions(): List<Session>

    /**
     * 세션 검색
     */
    suspend fun searchSessions(query: String): List<Session>

    /**
     * 세션 ID로 조회
     */
    suspend fun getSessionById(id: Long): Session?
    
    suspend fun createSession(): Long


    suspend fun deleteSession(id: Long)


    fun observeSessions(): Flow<List<Session>>
}
