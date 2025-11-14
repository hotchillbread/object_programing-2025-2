package com.example.logtalk.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ⚠️ Domain 레이어의 UseCase는 프로젝트에 맞게 정의하고 Hilt 모듈에 바인딩해야 합니다.
// 임시 인터페이스 정의 (실제 코드는 Domain Layer에 있어야 함)
interface SavePersonaUseCase { suspend operator fun invoke(persona: PersonaData) }
interface LoadPersonaUseCase { suspend operator fun invoke(): PersonaData }
interface DeleteAllRecordsUseCase { suspend operator fun invoke() }


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savePersonaUseCase: SavePersonaUseCase,
    private val loadPersonaUseCase: LoadPersonaUseCase,
    private val deleteAllRecordsUseCase: DeleteAllRecordsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // 초기 Persona Data 로드 시도
            val loadedPersona = try { loadPersonaUseCase() } catch (e: Exception) { PersonaData() }
            _uiState.update { 
                it.copy(persona = loadedPersona, currentEditingPersona = loadedPersona)
            }
        }
    }

    fun sendIntent(intent: SettingsIntent) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    private suspend fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            // 💡 편집 모드 진입 및 취소
            SettingsIntent.ClickEditPersona -> {
                _uiState.update { 
                    it.copy(isEditingPersona = true, currentEditingPersona = it.persona) // 원본을 임시 상태로 복사
                }
            }
            SettingsIntent.CancelEdit -> {
                _uiState.update { 
                    it.copy(isEditingPersona = false, currentEditingPersona = it.persona) // 원본으로 복구
                }
            }
            // 💡 텍스트 입력 시 임시 상태 업데이트
            is SettingsIntent.UpdateEditingDescription -> {
                _uiState.update { 
                    it.copy(currentEditingPersona = it.currentEditingPersona.copy(description = intent.newDescription))
                }
            }
            // 💡 데이터 저장
            is SettingsIntent.SavePersona -> {
                savePersonaUseCase(intent.persona)
                _uiState.update { 
                    it.copy(persona = intent.persona, isEditingPersona = false)
                }
            }
            
            // 💡 4단계: 삭제 관련 로직 (다음 단계에서 완성)
            else -> {} 
        }
    }
}