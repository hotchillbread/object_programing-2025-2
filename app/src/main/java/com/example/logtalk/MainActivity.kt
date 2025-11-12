package com.example.logtalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.logtalk.ui.login.LoginScreen
import com.example.logtalk.ui.theme.LogTalkTheme
import com.example.logtalk.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //하단 컨트롤러 숨기기(
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        //안드로이드 시스템가 차지하는 영역 뒤쪽까지 화면 전체에 걸쳐 보이도록
        enableEdgeToEdge()
        setContent {
            LogTalkTheme {
                AppNavigation()
            }
        }
    }
}
