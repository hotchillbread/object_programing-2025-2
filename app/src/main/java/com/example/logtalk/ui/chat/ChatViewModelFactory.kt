package com.example.logtalk.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.logtalk.domain.chat.*
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel

//DI주입을 위한 모델을 분리하는게 좋다고 gemini가 그래요..
class ChatViewModelFactory(
    private val initialTitleId: Long,
    // 실제 DI 컨테이너에서는 이 Use Case들을 주입받습니다.
    private val createNewChatUseCase: CreateNewChatUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val generateAndSaveTitleUseCase: GenerateAndSaveTitleUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(
                initialTitleId = initialTitleId,
                createNewChatUseCase = createNewChatUseCase,
                getChatHistoryUseCase = getChatHistoryUseCase,
                sendMessageUseCase = sendMessageUseCase,
                deleteChatUseCase = deleteChatUseCase,
                generateAndSaveTitleUseCase = generateAndSaveTitleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}