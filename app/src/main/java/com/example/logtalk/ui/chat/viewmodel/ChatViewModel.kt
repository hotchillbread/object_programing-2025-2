package com.example.logtalk.ui.chat.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logtalk.domain.chat.CreateNewChatUseCase
import com.example.logtalk.domain.chat.DeleteChatUseCase
import com.example.logtalk.domain.chat.GenerateAndSaveTitleUseCase
import com.example.logtalk.domain.chat.GetChatHistoryUseCase
import com.example.logtalk.domain.chat.SendMessageUseCase
import com.example.logtalk.ui.chat.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val createNewChatUseCase: CreateNewChatUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val generateAndSaveTitleUseCase: GenerateAndSaveTitleUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatState())
    val uiState = _uiState.asStateFlow()

    val sessionId: Long = savedStateHandle.get<Long>("sessionId") ?: -1L

    init {
        if (sessionId != -1L) {
            loadChatHistory(sessionId)
        }
    }

    private fun loadChatHistory(sessionId: Long) {
        viewModelScope.launch {
            // Implement chat history loading
        }
    }

    fun sendMessage() {
        // Implement send message
    }

    fun updateTextInput(text: String) {
        // Implement text input update
    }

    fun sendVoiceMessage() {
        // Implement voice message
    }

    fun findSimilarConsultation() {
        // No longer needed here, the screen will handle it
    }

    fun reportChat() {
        // Implement report chat
    }

    fun deleteChat(onChatDeleted: () -> Unit) {
        // Implement delete chat
    }
}