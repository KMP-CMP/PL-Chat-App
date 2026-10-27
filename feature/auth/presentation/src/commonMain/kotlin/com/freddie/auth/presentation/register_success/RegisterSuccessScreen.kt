package com.freddie.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.account_successfully_created
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.resend_verification_email
import chirp.feature.auth.presentation.generated.resources.resent_verification_email
import chirp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import com.freddie.core.designsystem.brand.ChirpSuccessIcon
import com.freddie.core.designsystem.buttons.ChirpButton
import com.freddie.core.designsystem.buttons.ChirpButtonStyle
import com.freddie.core.designsystem.layout.ChirpAdaptiveResultLayout
import com.freddie.core.designsystem.layout.ChirpSimpleSuccessLayout
import com.freddie.core.designsystem.layout.ChirpSnackbarScaffold
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterSuccessRoot(
    viewModel: RegisterSuccessViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // remember로 같은 SnackbarHostState를 유지해 재구성 중에도 진행 중인 Snackbar 상태를 보존한다.
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            is RegisterSuccessEvent.ResendVerificationEmailSuccess -> {
                // showSnackbar는 표시가 끝날 때까지 중단되므로 ObserveAsEvents의 코루틴에서 호출한다.
                // https://developer.android.com/develop/ui/compose/components/snackbar
                snackbarHostState.showSnackbar(
                    message = getString(
                        resource = Res.string.resent_verification_email
                    )
                )
            }
        }
    }

    RegisterSuccessScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    ChirpSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        ChirpAdaptiveResultLayout {
            ChirpSimpleSuccessLayout(
                title = stringResource(Res.string.account_successfully_created),
                // Compose Multiplatform의 stringResource는 %1$s를 첫 번째 인자로 치환한다.
                // 문장을 직접 이어 붙이지 않아 번역 리소스에서 이메일의 위치를 바꿀 수 있다.
                // https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#string-templates
                description = stringResource(
                    Res.string.verification_email_sent_to_x,
                    state.registeredEmail
                ),
                icon = {
                    ChirpSuccessIcon()
                },
                primaryButton = {
                    ChirpButton(
                        text = stringResource(Res.string.login),
                        onClick = {
                            onAction(RegisterSuccessAction.OnLoginClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                secondaryButton = {
                    ChirpButton(
                        text = stringResource(Res.string.resend_verification_email),
                        onClick = {
                            onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !state.isResendingVerificationEmail,
                        isLoading = state.isResendingVerificationEmail,
                        style = ChirpButtonStyle.SECONDARY
                    )
                },
                secondaryError = state.resendVerificationError?.asString()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "test@preview.com"
            ),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
