package com.freddie.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.freddie.auth.presentation.navigation.AuthGraphRoutes
import com.freddie.auth.presentation.navigation.authGraph

@Composable
fun NavigationRoot() {
    // rememberNavController는 재구성 시 같은 컨트롤러를 유지하고, 저장 가능한 내비게이션 상태를 복원한다.
    val navController = rememberNavController()
    // 그래프 자체를 시작 목적지로 지정하면 해당 그래프의 startDestination까지 진입한다.
    NavHost(
        navController = navController,
        startDestination = AuthGraphRoutes.Graph
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {

            }
        )
    }
}
