package com.freddie.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * 서버 요청 형식을 data 계층의 DTO로 분리하면, API 필드 구조가 바뀌어도
 * domain의 인증 계약과 요청 전송 형식을 독립적으로 조정할 수 있습니다.
 */
@Serializable
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)
