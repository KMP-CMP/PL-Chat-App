package com.freddie.core.domain.auth

import com.freddie.core.domain.util.DataError
import com.freddie.core.domain.util.EmptyResult

/**
 * 인증 계약을 domain에 두고 data의 Ktor 구현이 이를 따르게 하면,
 * 호출부는 HttpClient나 서버 요청 DTO 없이 인증 기능에 의존할 수 있습니다.
 */
interface AuthService {
    /**
     * EmptyResult<E>는 Result<Unit, E>의 별칭으로, 성공 데이터 없이 성공 여부와 오류를 표현합니다.
     * suspend는 호출 중 일시 중단을 허용하며, 그 자체로 백그라운드 스레드 실행을 보장하지 않습니다.
     * https://kotlinlang.org/docs/coroutines-basics.html
     */
    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote>
}
