package com.freddie.core.domain.validation

/**
 * 검증 결과를 조건별로 보존하면 UI에서 미충족 항목을 각각 안내할 수 있다.
 * 도메인에는 문자열 리소스 대신 검증 사실만 두어 표시 방식과 분리한다.
 */
data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasUppercase: Boolean = false,
) {
    // 계산 프로퍼티로 종합 결과를 도출해 개별 조건과 별도의 유효성 값이 불일치하지 않게 한다.
    val isValidPassword: Boolean
        get() = hasMinLength && hasDigit && hasUppercase
}
