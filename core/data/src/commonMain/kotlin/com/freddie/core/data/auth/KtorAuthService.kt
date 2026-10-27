package com.freddie.core.data.auth

import com.freddie.core.data.dto.requests.EmailRequest
import com.freddie.core.data.dto.requests.RegisterRequest
import com.freddie.core.data.networking.post
import com.freddie.core.domain.auth.AuthService
import com.freddie.core.domain.util.DataError
import com.freddie.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        // 반환 타입에서 Response는 Unit, body에서 Request는 RegisterRequest로 추론됩니다.
        // 공통 post 확장의 reified 타입 인자는 요청 직렬화와 응답 처리에 전달됩니다.
        // https://kotlinlang.org/docs/inline-functions.html#reified-type-parameters
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            ),
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(email),
        )
    }
}
