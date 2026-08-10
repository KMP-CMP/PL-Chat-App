package com.freddie.core.designsystem.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Material3 Snackbar는 조건부로 배치하는 컴포저블이 아니라 상태 기반 구조다:
 * hoisting된 SnackbarHostState에 suspend fun showSnackbar()로 메시지를 큐잉하면
 * snackbarHost 슬롯의 SnackbarHost가 표시·중복 처리·자동 해제를 담당한다.
 * https://developer.android.com/develop/ui/compose/components/snackbar
 */
@Composable
fun ChirpSnackbarScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        // contentWindowInsets: 기본값(systemBars) 대신 innerPadding에 반영할 인셋을 직접 지정한다.
        // union은 변마다 큰 값을 취해 인셋을 합성하고, ime 포함으로 키보드가 올라오면 innerPadding도 함께 커진다.
        // https://developer.android.com/develop/ui/compose/layouts/insets
        contentWindowInsets = WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .union(WindowInsets.ime),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 24.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}