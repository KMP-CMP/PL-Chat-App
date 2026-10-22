package com.freddie.core.domain.validation

/**
 * 입력만으로 결과를 만드는 검증 로직은 Compose나 플랫폼 API 없이 commonMain에서 공유할 수 있다.
 * UI 상태와 독립적이므로 화면을 구성하지 않고도 검증 규칙을 테스트할 수 있다.
 */
object PasswordValidator {

    private const val MIN_PASSWORD_LENGTH = 9

    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_PASSWORD_LENGTH,
            hasDigit = password.any { it.isDigit() },
            hasUppercase = password.any { it.isUpperCase() }
        )
    }
}
