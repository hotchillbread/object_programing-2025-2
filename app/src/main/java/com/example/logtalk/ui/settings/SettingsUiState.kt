package com.example.logtalk.ui.settings

data class SettingsUiState(
    val persona: PersonaData = PersonaData(),
    val isEditingPersona: Boolean = false,
    val showDeleteDialog: Boolean = false,

    // 💡 편집 중 임시로 데이터를 저장할 상태
    val currentEditingPersona: PersonaData = PersonaData(),

    val appInfo: AppInfoData = AppInfoData("1.0.0", "마음 멘토 팀", "support@maum.app")
)