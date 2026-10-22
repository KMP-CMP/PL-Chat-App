package com.freddie.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import com.freddie.auth.domain.EmailValidator
import com.freddie.core.domain.validation.PasswordValidator
import com.freddie.core.presentation.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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
            RegisterAction.OnLoginClick -> validateFormInputs()
            RegisterAction.OnInputTextFocusGain -> clearAllTextFieldErrors()
            else -> Unit
        }
    }

    private fun clearAllTextFieldErrors() {
        // update는 현재 값을 기준으로 원자적으로 갱신한다. 동시 갱신 시 람다가 재실행될 수 있어
        // copy처럼 부수 효과 없는 계산만 두고 네트워크 요청 등의 작업은 밖에서 수행한다.
        // https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/update.html
        _state.update {
            it.copy(
                emailError = null,
                usernameError = null,
                passwordError = null,
                registrationError = null
            )
        }
    }

    private fun validateFormInputs(): Boolean {
        clearAllTextFieldErrors()
        
        val currentState = state.value
        // TextFieldState가 입력을 직접 보유하므로 제출 시 text를 String으로 읽어 검증한다.
        // 별도의 onValueChange 콜백으로 검증용 문자열 상태를 동기화할 필요가 없다.
        // https://developer.android.com/develop/ui/compose/text/user-input
        val email = currentState.emailTextState.text.toString()
        val username = currentState.usernameTextState.text.toString()
        val password = currentState.passwordTextState.text.toString()

        val isEmailValid = EmailValidator.validate(email)
        val passwordValidationState = PasswordValidator.validate(password)
        val isUsernameValid = username.length in 3..20

        // UiText.Resource는 문자열 대신 리소스 참조를 보관하고, UI의 asString()에서 문구를 해석한다.
        // Res.string은 Compose Multiplatform의 StringResource이므로 Android Context 없이 전달할 수 있다.
        // https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html
        val emailError = if (!isEmailValid) {
            UiText.Resource(Res.string.error_invalid_email)
        } else null
        val usernameError = if (!isUsernameValid) {
            UiText.Resource(Res.string.error_invalid_username)
        } else null
        val passwordError = if (!passwordValidationState.isValidPassword) {
            UiText.Resource(Res.string.error_invalid_password)
        } else null
        _state.update {
            it.copy(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        return isUsernameValid && isEmailValid && passwordValidationState.isValidPassword
    }
}
