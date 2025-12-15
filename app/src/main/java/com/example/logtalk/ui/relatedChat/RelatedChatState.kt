package com.example.logtalk.ui.relatedChat

data class RelatedChatState(
    val isLoading: Boolean = false,
    val relatedChats: List<RelatedChat> = emptyList(),
    val error: String? = null
)
