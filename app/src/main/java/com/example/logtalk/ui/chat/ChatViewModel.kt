    package com.example.logtalk.ui.chat.viewmodel

    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.setValue
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.logtalk.domain.chat.CreateNewChatUseCase
    import com.example.logtalk.domain.chat.DeleteChatUseCase
    import com.example.logtalk.domain.chat.GenerateAndSaveTitleUseCase // 새 Use Case 필요
    import com.example.logtalk.domain.chat.GetChatHistoryUseCase
    import com.example.logtalk.domain.chat.SendMessageUseCase
    import com.example.logtalk.ui.chat.data.ChatUiState
    import com.example.logtalk.ui.chat.data.Title // Title 모델 필요
    import kotlinx.coroutines.flow.collectLatest
    import kotlinx.coroutines.launch


    class ChatViewModel(
        private val initialTitleId: Long,
        private val createNewChatUseCase: CreateNewChatUseCase,
        private val getChatHistoryUseCase: GetChatHistoryUseCase,
        private val sendMessageUseCase: SendMessageUseCase,
        private val deleteChatUseCase: DeleteChatUseCase,
        private val generateAndSaveTitleUseCase: GenerateAndSaveTitleUseCase // 제목 생성 Use Case 추가
    ) : ViewModel() {

        // UI 상태
        var uiState by mutableStateOf(ChatUiState())
            private set

        // 현재 활성화된 채팅방 ID
        private var currentTitleId: Long = initialTitleId

        // 첫 메시지 전송 여부 (제목 생성을 한 번만 하기 위함)
        private var isFirstMessageSent: Boolean = false

        // 현재 채팅방의 Title 객체 (삭제 등을 위해 필요)
        private var currentChatTitle: Title? = null


        init {
            // ViewModel 초기화 시, 채팅방 ID에 따라 로직 분기
            handleChatInitialization()
        }

        // --- 1. 초기화 및 데이터 로딩 ---

        private fun handleChatInitialization() {
            viewModelScope.launch {
                if (currentTitleId == -1L) {
                    // 1.1. 새 채팅방 생성
                    currentTitleId = createNewChatUseCase()
                    // 새 채팅방은 당연히 첫 메시지 전송 전 상태입니다.
                    isFirstMessageSent = false
                } else {
                    // 1.2. 기존 채팅방 기록 로드
                    // 기존 채팅방은 이미 제목이 생성되어 있어야 합니다.
                    isFirstMessageSent = true
                }

                // 데이터 구독 시작
                observeChatHistory(currentTitleId)
            }
        }

        // 메시지 기록 Flow 구독 및 UI 상태 업데이트
        private fun observeChatHistory(titleId: Long) {
            viewModelScope.launch {
                getChatHistoryUseCase(titleId).collectLatest { messages ->
                    uiState = uiState.copy(
                        messages = messages,
                        // 메시지 로드 시 로딩 상태 해제
                        isLoading = false
                    )
                    // 현재 메시지 수를 기반으로 첫 메시지 전송 여부 업데이트 (앱 재시작 시 필요)
                    isFirstMessageSent = messages.size >= 2 // 사용자 메시지 + 봇 응답이 있으면 제목 생성 완료로 간주
                }
            }
        }

        // --- 2. 입력 및 상태 변경 ---

        // 입력 텍스트 변경
        fun updateTextInput(newText: String) {
            uiState = uiState.copy(textInput = newText)
        }

        // --- 3. 핵심 로직: 메시지 전송 ---

        fun sendMessage() {
            if (uiState.textInput.isBlank() || uiState.isLoading) return

            val userMessageText = uiState.textInput
            val history = uiState.messages

            // 1. UI 상태 즉시 업데이트 (입력 필드 초기화 및 로딩 시작)
            uiState = uiState.copy(
                textInput = "",
                isLoading = true
            )

            viewModelScope.launch {
                try {
                    // 2. Use Case를 통해 메시지 전송 및 응답 처리
                    sendMessageUseCase(userMessageText, history, currentTitleId)

                    // 3. 첫 메시지 전송 완료 시 제목 생성 로직 실행
                    if (!isFirstMessageSent) {
                        // 첫 메시지(userMessageText)를 기반으로 제목 생성 요청
                        generateAndSaveTitleUseCase(currentTitleId, userMessageText)
                        isFirstMessageSent = true
                    }

                } catch (e: Exception) {
                    // 오류 처리
                    uiState = uiState.copy(
                        errorMessage = "메시지 전송 실패: ${e.localizedMessage}",
                    )
                } finally {
                    uiState = uiState.copy(
                        isLoading = false
                    )
                }
            }
        }

        // --- 4. 메뉴 액션 처리 (Composable과 연동) ---

        fun reportChat() {
            // TODO: 신고 Use Case 구현 후 연결
            viewModelScope.launch {
                // chatRepository.reportChatHistory() // 구현 예정
                uiState = uiState.copy(errorMessage = "신고가 접수되었습니다. (로직 임시 처리)")
            }
        }

        fun deleteChat(onChatDeleted: () -> Unit) {
            viewModelScope.launch {
                try {
                    // 삭제를 위해 현재 Title 객체 생성 (ID 기반)
                    val titleToDelete = Title(titleId = currentTitleId, title = "", embedding = null, createdAt = 0L)

                    deleteChatUseCase(titleToDelete)

                    // 삭제 완료 후 네비게이션 콜백 호출
                    onChatDeleted()
                } catch (e: Exception) {
                    uiState = uiState.copy(errorMessage = "채팅방 삭제 실패: ${e.localizedMessage}")
                }
            }
        }


        fun findSimilarConsultation() {
            // 라우팅은 UI/Composable 레이어에서 처리
            uiState = uiState.copy(errorMessage = "유사 상담 찾기 화면으로 이동합니다.")
        }

        fun sendVoiceMessage() {
            // TODO: 음성 메시지 로직 구현
            uiState = uiState.copy(errorMessage = "음성 메시지 기능은 준비 중입니다.")
        }
    }