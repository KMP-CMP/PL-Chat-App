package com.freddie.auth.domain

object EmailValidator {

    private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"

    fun validate(email: String): Boolean {
        // Kotlin Regex는 commonMain에서 사용할 수 있어 Android 전용 Patterns에 의존하지 않는다.
        // matches는 부분 검색이 아니라 입력 전체가 패턴에 맞는지 검사한다.
        // https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/-regex/matches.html
        return EMAIL_PATTERN.toRegex().matches(email)
    }
}
