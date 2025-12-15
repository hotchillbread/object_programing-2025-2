package com.example.logtalk.ui.relatedChat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logtalk.ui.chat.composable.LogTalkAppBar
import com.example.logtalk.ui.relatedChat.composable.RelatedChatList

@Composable
fun RelatedChatScreen(
    onBackClick: () -> Unit,
    viewModel: RelatedChatViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            LogTalkAppBar(
                onBackClick = onBackClick,
                onFindSimilarClick = { },
                onReportClick = { },
                onDeleteChatClick = { }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "방금 상담과 비슷한 상담 목록",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            RelatedChatList(chats = uiState.relatedChats)
        }
    }
}
