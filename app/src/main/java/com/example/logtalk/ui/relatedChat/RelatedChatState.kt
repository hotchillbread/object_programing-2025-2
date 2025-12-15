package com.example.logtalk.ui.relatedChat

/**
 * UI 상태를 나타내는 데이터 클래스입니다.
 *
 * @param isLoading 데이터 로딩 중인지 여부
 * @param relatedChats 유사 상담 목록
 * @param error 오류 메시지
 */
data class RelatedChatState(
    val isLoading: Boolean = false,
    val relatedChats: List<RelatedChat> = emptyList(),
    val error: String? = null
)

/**
 * 개별 유사 상담 항목을 나타내는 데이터 클래스입니다.
 *
 * @param date 날짜
 * @param title 제목
 * @param summary 요약 내용
 */
data class RelatedChat(
    val date: String,
    val title: String,
    val summary: String
)
