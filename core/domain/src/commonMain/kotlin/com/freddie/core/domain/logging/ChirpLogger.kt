package com.freddie.core.domain.logging

// 도메인이 로깅 구현 대신 이 추상화에 의존하면 구현 교체와 테스트 대역 주입이 쉬워집니다.
// https://developer.android.com/training/dependency-injection
interface ChirpLogger {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String, throwable: Throwable? = null)
}
