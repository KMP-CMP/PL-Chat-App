package com.freddie.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class RegisterViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    // onStart + stateIn: init 블록 대신 "첫 구독 시점"에 초기 로딩을 거는 관용구다.
    // 구독자가 없으면 로딩 자체를 하지 않고, 재구독(화면 회전 등)마다 onStart가 다시 실행되므로
    // hasLoadedInitialData 플래그로 중복 로딩을 막는다.
    // https://developer.android.com/topic/architecture/ui-layer/state-production
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            // WhileSubscribed(5_000L): 마지막 구독자가 사라져도 5초간 upstream을 살려둔다.
            // 화면 회전·짧은 백그라운드 전환에서 흐름을 끊었다 다시 수집하는 낭비를 피하는 관용적 값.
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    fun onAction(action: RegisterAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}