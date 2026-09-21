package com.freddie.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.email
import chirp.feature.auth.presentation.generated.resources.email_placeholder
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.password
import chirp.feature.auth.presentation.generated.resources.password_hint
import chirp.feature.auth.presentation.generated.resources.register
import chirp.feature.auth.presentation.generated.resources.username
import chirp.feature.auth.presentation.generated.resources.username_hint
import chirp.feature.auth.presentation.generated.resources.username_placeholder
import chirp.feature.auth.presentation.generated.resources.welcome_to_chirp
import com.freddie.core.designsystem.brand.ChirpBrandLogo
import com.freddie.core.designsystem.buttons.ChirpButton
import com.freddie.core.designsystem.buttons.ChirpButtonStyle
import com.freddie.core.designsystem.layout.ChirpAdaptiveFormLayout
import com.freddie.core.designsystem.layout.ChirpSnackbarScaffold
import com.freddie.core.designsystem.textfield.ChirpPasswordTextField
import com.freddie.core.designsystem.textfield.ChirpTextField
import com.freddie.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root/Screen 분리: Root만 ViewModel과 상태 수집을 알고, Screen은 state와 onAction만 받는
 * stateless 컴포저블이라 Preview·테스트에서 ViewModel 없이 그대로 렌더링할 수 있다.
 * https://developer.android.com/develop/ui/compose/state-hoisting
 */
@Composable
fun RegisterRoot(
    // koinViewModel(): DI 컨테이너에서 ViewModel을 꺼낸다. 기본 viewModel()은 리플렉션으로
    // 인스턴스를 생성해 Android(JVM)에선 동작하지만, 리플렉션이 없는 Kotlin/Native(iOS)에선
    // UnsupportedOperationException으로 크래시한다 — KMP에선 팩토리 주입이 필수인 이유.
    // https://insert-koin.io/docs/reference/koin-compose/compose/
    viewModel: RegisterViewModel = koinViewModel()
) {
    // collectAsStateWithLifecycle: collectAsState와 달리 Lifecycle이 STARTED 아래로 내려가면
    // 수집을 멈춘다. ViewModel의 WhileSubscribed와 짝을 이뤄 백그라운드에서 upstream까지 정지시킨다.
    // https://developer.android.com/topic/libraries/architecture/compose#collectasstatewithlifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    ChirpSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_to_chirp),
            errorText = state.registrationError?.asString(),
            logo = { ChirpBrandLogo() }
        ) {
            ChirpTextField(
                state = state.usernameTextState,
                placeholder = stringResource(Res.string.username_placeholder),
                title = stringResource(Res.string.username),
                supportingText = state.usernameError?.asString()
                    ?: stringResource(Res.string.username_hint),
                isError = state.usernameError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChirpTextField(
                state = state.emailTextState,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ChirpPasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                supportingText = state.passwordError?.asString()
                    ?: stringResource(Res.string.password_hint),
                isError = state.passwordError != null,
                onFocusChanged = { isFocused ->
                    onAction(RegisterAction.OnInputTextFocusGain)
                },
                onToggleVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.height(16.dp))

            ChirpButton(
                text = stringResource(Res.string.register),
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                enabled = state.canRegister,
                isLoading = state.isRegistering,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChirpButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
                style = ChirpButtonStyle.SECONDARY,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}