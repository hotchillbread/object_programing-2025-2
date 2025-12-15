package com.example.logtalk.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logtalk.ui.login.LoginScreen
import com.example.logtalk.MainScreen
import com.example.logtalk.ui.relatedChat.RelatedChatScreen

@Composable
fun AppNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = AppGraph.AUTHENTICATION,
        route = AppGraph.ROOT
    ) {
        navigation(
            route = AppGraph.AUTHENTICATION,
            startDestination = AuthScreenRoutes.LOGIN
        ) {
            composable(AuthScreenRoutes.LOGIN) {
                LoginScreen(
                    onGoogleLoginClick = {
                        rootNavController.navigate(AppGraph.MAIN) {
                            popUpTo(AppGraph.AUTHENTICATION) {
                                inclusive = true
                            }
                        }
                    },
                    onSignUpClick = {
                        rootNavController.navigate(AuthScreenRoutes.ONBOARDING)
                    }
                )
            }
            composable(AuthScreenRoutes.ONBOARDING) {
                Text("온보딩 화면", modifier = Modifier.fillMaxSize())
            }
        }

        navigation(
            route = AppGraph.MAIN,
            startDestination = "main_screen"
        ) {
            composable("main_screen") {
                MainScreen(rootNavController = rootNavController) // Pass NavController to MainScreen
            }
            composable(
                route = MainScreenRoutes.RelatedChat.route + "/{sessionId}",
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: -1L
                RelatedChatScreen(
                    onBackClick = { rootNavController.popBackStack() },
                    sessionId = sessionId
                )
            }
        }
    }
}