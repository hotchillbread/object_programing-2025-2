package com.example.logtalk


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.logtalk.ui.chat.ChatViewModelFactory
import com.example.logtalk.ui.chat.data.Message
import com.example.logtalk.ui.chat.screen.ChatScreen
import com.example.logtalk.ui.chat.viewmodel.ChatViewModel
import com.example.logtalk.ui.navigation.MainScreenRoutes

import com.example.logtalk.ui.settings.SettingsScreen
import com.example.logtalk.ui.theme.LoginColors
import com.example.logtalk.ui.home.HomeScreen

@Composable
fun MainScreen() {
    val view = LocalView.current
    val window = (view.context as Activity).window

    SideEffect {
        window.statusBarColor = Color.White.toArgb()
        window.navigationBarColor = Color.White.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
    }
    // nav 생성
    val mainNavController = rememberNavController()

    // 하단 탭 설정
    val items = listOf(
        MainScreenRoutes.Home,
        MainScreenRoutes.Settings,
    )

    Scaffold(
        bottomBar = {
            Column {

                Divider(
                    color = Color.LightGray.copy(alpha=0.8f),
                    thickness = 0.5.dp
                )

                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.height(72.dp),
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { screen ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                mainNavController.navigate(screen.route) {
                                    popUpTo(mainNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(top = 0.dp, bottom = 0.dp)
                                ) {
                                    Icon(
                                        screen.icon,
                                        contentDescription = screen.label,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha=0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(0.dp))
                                    Text(
                                        text = screen.label,
                                        fontSize =14.sp,
                                        color = if (isSelected) LoginColors.TextPurple else LoginColors.TextGray.copy(alpha=0.8f)
                                    )
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = LoginColors.TextPurple,
                                unselectedIconColor = LoginColors.TextGray,
                                selectedTextColor = LoginColors.TextPurple,
                                unselectedTextColor = LoginColors.TextGray,
                                indicatorColor = LoginColors.BackgroundWhite,
                            )
                        )

                    }
                }
            }
        }
    ) { innerPadding ->
        //화면 내부에 nav 호스팅
        NavHost(
            navController = mainNavController,
            startDestination = MainScreenRoutes.Home.route, //시작은 home부터 하도록 설정
            modifier = Modifier.padding(innerPadding)

        ) {

            composable(MainScreenRoutes.Home.route) {
                HomeScreen( /*
                    onChatSelected = { titleId ->
                        mainNavController.navigate("chat/$titleId")
                    },
                    onNewChatClicked = {
                        mainNavController.navigate("chat/-1")
                    }*/ //라우팅 추가로 연결해야함
                )
            }

            //chat route
            composable(
                route = MainScreenRoutes.Chat.route, // "chat/{titleId}" 경로가 되도록 가정
                arguments = listOf(navArgument("titleId") {
                    type = NavType.LongType
                    defaultValue = -1L // 값이 없으면 -1L (새 채팅)
                })
            ) { backStackEntry ->
                // 2.1. Navigation 인수 추출
                val initialTitleId = backStackEntry.arguments?.getLong("titleId") ?: -1L

                // 2.2. ViewModel Factory를 사용하여 ViewModel 인스턴스 생성 및 파라미터 주입
                // 🚨 주의: 아래 Use Case 인스턴스는 실제 앱의 DI 컨테이너에서 가져와야 합니다.
                // 여기서는 주입이 가능하다는 가정 하에 코드를 완성합니다.
                val chatViewModel: ChatViewModel = viewModel(
                    // NOTE: 아래 코드는 Factory와 모든 Use Case 인스턴스가 사용 가능해야 합니다.
                    factory = ChatViewModelFactory(
                        initialTitleId = initialTitleId,

                        createNewChatUseCase = createNewChatUseCaseInstance,
                        getChatHistoryUseCase = getChatHistoryUseCaseInstance,
                        sendMessageUseCase = sendMessageUseCaseInstance,
                        deleteChatUseCase = deleteChatUseCaseInstance,
                        generateAndSaveTitleUseCase = generateAndSaveTitleUseCaseInstance
                    )
                )

                // 2.3. ChatScreen 호출 및 모든 콜백 연결
                ChatScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    },
                    // TODO: 유사 상담 화면으로 이동하는 Navigation 로직 연결
                    onNavigateToSimilarConsultation = {
                        // mainNavController.navigate("similar_consultation_route")
                        // 임시로 뒤로가기 대신 로그를 남김
                        println("DEBUG: Navigate to Similar Consultation")
                    },
                    viewModel = chatViewModel
                )
            }
            //여기서 viewmodel 라우팅 해줘야돼요!!!!!! 꼭 하자 OK?
            composable(MainScreenRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        mainNavController.popBackStack() // HomeScreen으로 돌아가기
                    }
                )
            }
        }
    }
}

// 테스트용 더미 데이터
val dummyMessages: List<Message> = listOf(
    // 1. 봇 메시지 (ID: 1)
    Message(
        id = 1L,
        text = "안녕하세요! 로그톡 봇입니다. 어떤 고민이 있으신가요?",
        isUser = false
    ),

    // 2. 사용자 메시지 (ID: 2)
    Message(
        id = 2L,
        text = "요즘 진로 문제 때문에 고민이 많아요. 전공을 바꿔야 할까요?",
        isUser = true
    ),

    // 3. 봇 메시지 - 관련 상담 제안 포함 (ID: 3)
    // relatedConsultation, relatedDate, directQuestion 필드를 활용한 예시
    Message(
        id = 3L,
        text = "사용자님의 고민과 비슷한 내용을 이전에 상담하셨습니다. 관련 상담 내용을 참고해보시는 건 어떨까요?",
        isUser = false,
        relatedConsultation = "지난 상담에서는 이직을 고민하셨고, 결국 성공적인 결정을 내리셨습니다.",
        relatedDate = "2025.11.02",
        directQuestion = "지금 느끼는 불안감의 핵심 원인은 무엇이라고 생각하시나요?"
    ),

    // 4. 사용자 메시지 (ID: 4)
    Message(
        id = 4L,
        text = "네, 그때와 비슷한 복잡한 감정인 것 같아요.",
        isUser = true
    ),

    // 5. 봇 메시지 (ID: 5)
    Message(
        id = 5L,
        text = "이해합니다. 그럼 저희가 함께 이 문제를 깊이 있게 탐색해 봅시다.",
        isUser = false
    )
)