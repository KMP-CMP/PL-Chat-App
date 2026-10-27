package com.freddie.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freddie.core.domain.auth.AuthService
import com.freddie.core.domain.util.onFailure
import com.freddie.core.domain.util.onSuccess
import com.freddie.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onFailure
import kotlin.onSuccess

class RegisterSuccessViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var hasLoadedInitialData = false

    // receiveAsFlow는 이벤트 하나를 한 수집자에게 전달한다. StateFlow처럼 최신 값을 재전달하지 않는다.
    // 기본 Channel은 버퍼가 없어 수집자가 없으면 send가 대기하며, 취소 시 처리 보장까지 제공하지는 않는다.
    // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/receive-as-flow.html
    private val eventChannel = Channel<RegisterSuccessEvent>()
    val events = eventChannel.receiveAsFlow()

    private val email = savedStateHandle.get<String>("email")
        ?: throw IllegalStateException("No email passed to register success screen")
    private val _state = MutableStateFlow(
        RegisterSuccessState(
            registeredEmail = email
        )
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            // stateIn의 초기값은 _state.value와 별개다. upstream의 첫 값이 전달되기 전에는
            // registeredEmail이 빈 문자열이며, 수집이 시작되어야 _state의 전달받은 이메일로 갱신된다.
            // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/state-in.html
            initialValue = RegisterSuccessState()
        )

    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            is RegisterSuccessAction.OnResendVerificationEmailClick -> resendVerification()
            else -> Unit
        }
    }

    private fun resendVerification() {
        if (state.value.isResendingVerificationEmail) {
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isResendingVerificationEmail = true,
                )
            }

            authService
                .resendVerificationEmail(email)
                .onSuccess {
                    _state.update { it.copy(
                        isResendingVerificationEmail = false
                    ) }
                    eventChannel.send(RegisterSuccessEvent.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        isResendingVerificationEmail = false,
                        resendVerificationError = error.toUiText()
                    ) }
                }
        }
    }

}
