package com.example.logtalk.ui.relatedChat.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.logtalk.ui.relatedChat.RelatedChat

@Composable
fun RelatedChatList(chats: List<RelatedChat>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chats) { chat ->
            RelatedChatItem(chat = chat)
        }
    }
}
