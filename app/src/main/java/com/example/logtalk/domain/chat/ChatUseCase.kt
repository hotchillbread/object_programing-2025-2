package com.example.logtalk.domain.chat

import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.domain.chat.ChatRepository

class SendMessageUseCase(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userMessageText: String,
        history: List<Message>,
        parentTitleId: Long
    ): Message {
        //메세지 객체 생성 & 저장
        val userMessage = Message(
            id = System.currentTimeMillis(),
            text = userMessageText,
            isUser = true

        )
        chatRepository.saveMessage(userMessage, parentTitleId)

        //응답 요청
        val botResponse = chatRepository.getBotResponse(userMessageText, history + userMessage)
        //응답 객체 생성
        val botMessage = Message(
            id = System.currentTimeMillis() + 1, // 충돌 방지를 위해 +1
            text = botResponse,
            isUser = false
        )

        //응답 저장
        chatRepository.saveMessage(botMessage, parentTitleId)

        return botMessage
    }
}