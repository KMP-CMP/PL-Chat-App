package com.freddie.auth.presentation.register_success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class RegisterSuccessViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(
        RegisterSuccessState(
            registeredEmail = "test@test.com"
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
            // registeredEmail이 빈 문자열이며, 수집이 시작되어야 _state의 테스트 이메일로 갱신된다.
            // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/state-in.html
            initialValue = RegisterSuccessState()
        )

    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}
