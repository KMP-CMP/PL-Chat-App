package com.freddie.auth.presentation.email_varification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class EmailVerificationViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state
        // WhileSubscribed는 마지막 구독자가 떠난 뒤 5초가 지나면 상위 Flow 수집을 멈춘다.
        // 이후 다시 구독하면 onStart도 재실행되므로 플래그로 초기화 블록을 ViewModel당 한 번만 실행한다.
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EmailVerificationState()
        )

    fun onAction(action: EmailVerificationAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
