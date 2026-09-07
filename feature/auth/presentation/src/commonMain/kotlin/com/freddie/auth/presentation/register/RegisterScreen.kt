package com.freddie.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freddie.core.designsystem.theme.ChirpTheme

/**
 * Root/Screen 분리: Root만 ViewModel과 상태 수집을 알고, Screen은 state와 onAction만 받는
 * stateless 컴포저블이라 Preview·테스트에서 ViewModel 없이 그대로 렌더링할 수 있다.
 * https://developer.android.com/develop/ui/compose/state-hoisting
 */
@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = viewModel()
) {
    // collectAsStateWithLifecycle: collectAsState와 달리 Lifecycle이 STARTED 아래로 내려가면
    // 수집을 멈춘다. ViewModel의 WhileSubscribed와 짝을 이뤄 백그라운드에서 upstream까지 정지시킨다.
    // https://developer.android.com/topic/libraries/architecture/compose#collectasstatewithlifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {

}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}