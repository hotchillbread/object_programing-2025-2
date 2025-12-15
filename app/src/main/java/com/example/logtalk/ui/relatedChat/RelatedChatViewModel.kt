package com.example.logtalk.ui.relatedChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RelatedChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RelatedChatState())
    val uiState: StateFlow<RelatedChatState> = _uiState

    init {
        fetchRelatedChats()
    }

    private fun fetchRelatedChats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // In a real app, you would fetch this from a repository or use case
            val chats = getDummyRelatedChats()
            _uiState.value = _uiState.value.copy(isLoading = false, relatedChats = chats)
        }
    }

    private fun getDummyRelatedChats(): List<RelatedChat> {
        return listOf(
            RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
            RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
            RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
            RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해")
        )
    }
}
