package com.freddie.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

@Composable
fun DeepLinkListener(
    navController: NavController
) {
    // DisposableEffect는 이 호출이 Composition에 들어올 때 리스너를 등록하고, 떠날 때 onDispose로 컨트롤러 참조를 해제한다.
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(NavUri(uri))
        }

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}