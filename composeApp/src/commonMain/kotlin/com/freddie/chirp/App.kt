package com.freddie.chirp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.freddie.chirp.navigation.DeepLinkListener
import com.freddie.chirp.navigation.NavigationRoot
import com.freddie.core.designsystem.theme.ChirpTheme

@Composable
@Preview
fun App() {
    // rememberNavController는 재구성 시 같은 컨트롤러를 유지하고, 저장 가능한 내비게이션 상태를 복원한다.
    val navController = rememberNavController()
    DeepLinkListener(navController)
    ChirpTheme {
        NavigationRoot(navController)
    }
}