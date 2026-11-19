package com.freddie.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.freddie.auth.presentation.navigation.AuthGraphRoutes
import com.freddie.auth.presentation.navigation.authGraph

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
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
