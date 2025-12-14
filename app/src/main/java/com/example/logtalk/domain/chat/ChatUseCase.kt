package com.example.logtalk.domain.chat

import ChatRepository
import com.example.logtalk.core.utils.Logger
import com.example.logtalk.core.utils.model.OpenIllegitimateSummarize
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.data.Title
import kotlinx.coroutines.flow.Flow

// --- 1. 메시지 전송 및 응답 ---
class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userMessageText: String,
        history: List<Message>,
        parentTitleId: Long
    ): Message {
        // 1. 사용자 메시지 객체 생성 및 저장
        val userMessage = Message(
            // DB에 저장 시 messageId가 자동 생성되므로, 0L 또는 임시값 사용
            id = 0L,
            text = userMessageText,
            isUser = true
        )
        chatRepository.saveMessage(userMessage, parentTitleId)

        // 2. 응답 요청 (사용자 메시지 포함)
        val botResponse = chatRepository.getBotResponse(userMessageText, history + userMessage)

        // 3. 봇 응답 객체 생성 및 저장
        val botMessage = Message(
            id = 0L, // DB에 저장 시 messageId가 자동 생성되도록 0L 사용
            text = botResponse,
            isUser = false
        )
        chatRepository.saveMessage(botMessage, parentTitleId)

        return botMessage
    }
}

// --- 2. 새 채팅방 생성 ---
class CreateNewChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Long {
        // 새 채팅방 생성을 위한 초기 Title 객체
        val newTitle = Title(
            titleId = 0L, // Room에서 자동 생성될 것입니다.
            title = "새 대화", // 임시 제목
            embedding = null,
            createdAt = System.currentTimeMillis()
        )

        // ChatRepository를 통해 insertTitle 호출 및 ID 반환
        return chatRepository.insertTitle(newTitle)
    }
}

// --- 3. 채팅방 목록 조회 ---
class GetChatListUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Title>> {
        // ChatRepository를 통해 TitleDao.getAllTitles 호출
        return chatRepository.getAllTitles()
    }
}

// --- 4. 채팅 기록 조회 ---
class GetChatHistoryUseCase(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(parentTitleId: Long): Flow<List<Message>> {
        // ChatRepository를 통해 MessageDao.getMessagesByParentTitleId 호출
        return chatRepository.getMessagesByParentTitleId(parentTitleId)
    }
}

// --- 5. 채팅방 삭제 ---
class DeleteChatUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(title: Title) {
        // 1. Title 객체 삭제 (Room의 CASCADE 설정으로 메시지도 함께 삭제됨)
        chatRepository.deleteTitle(title)

        // 2. (선택 사항) LLM 히스토리 초기화 (삭제된 대화에 대한 컨텍스트 정리)
        chatRepository.resetHistory()
    }
}

class GenerateAndSaveTitleUseCase(
    private val chatRepository: ChatRepository,
    private val titleSummarizer: OpenIllegitimateSummarize // 요약 전용 LLM
) {

    suspend operator fun invoke(titleId: Long, firstMessageText: String) {

        // LLM에게 제목 요약 요청
        val newTitleText = titleSummarizer.getResponse(firstMessageText)

        // DB 업데이트
        chatRepository.updateTitleText(titleId, newTitleText)


        Logger.d("$titleId: 제목 업데이트=> '$newTitleText'")
    }
}