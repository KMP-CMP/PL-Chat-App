package com.freddie.auth.presentation.email_varification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freddie.core.domain.auth.AuthService
import com.freddie.core.domain.util.onFailure
import com.freddie.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false
    // 화면 진입 시 전달한 인자는 SavedStateHandle에서 같은 키로 읽을 수 있다.
    // "token" 키가 없으면 get<String>()은 null을 반환한다.
    private val token = savedStateHandle.get<String>("token")

    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state
        // WhileSubscribed는 마지막 구독자가 떠난 뒤 5초가 지나면 상위 Flow 수집을 멈춘다.
        // 이후 다시 구독하면 onStart도 재실행되므로 플래그로 초기화 블록을 ViewModel당 한 번만 실행한다.
        .onStart {
            if (!hasLoadedInitialData) {
                verifyEmail()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EmailVerificationState()
        )

    // NO-OP: Actions are purely for navigation
    fun onAction(action: EmailVerificationAction) = Unit

    private fun verifyEmail() {
        viewModelScope.launch {
            // update는 최신 상태를 기준으로 copy를 원자적으로 적용한다.
            // 동시에 상태가 바뀌면 람다가 다시 실행될 수 있어 인증 요청은 람다 밖에서 호출한다.
            _state.update {
                it.copy(
                    isVerifying = true
                )
            }

            authService
                .verifyEmail(token = token ?: "Invalid token")
                .onSuccess {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = true
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = false
                        )
                    }
                }
        }
    }

}
