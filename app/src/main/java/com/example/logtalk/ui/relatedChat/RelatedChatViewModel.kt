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
            // Dummy data from the screenshot
            val dummyChats = listOf(
                RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해"),
                RelatedChat("2025.11.02", "너 생각에는 내가 재수강을 하는게 좋을까?", "아 그래? 넌 왜 그렇게 생각해? 3줄로 말해")
            )
            _uiState.value = _uiState.value.copy(isLoading = false, relatedChats = dummyChats)
        }
    }
}
