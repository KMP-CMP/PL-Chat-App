package com.freddie.core.data.networking

import com.freddie.core.data.BuildKonfig
import com.freddie.core.domain.logging.ChirpLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val chirpLogger: ChirpLogger
) {

    // 엔진을 외부에서 받으면 공통 설정을 공유하면서 Android는 OkHttp, iOS는 Darwin을 선택할 수 있습니다.
    // https://ktor.io/docs/client-engines.html
    fun create(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        // 모델에 없는 JSON 키를 무시해 서버의 필드 추가가 기존 앱의 역직렬화를 깨뜨리지 않게 합니다.
                        // https://kotlinlang.org/api/kotlinx.serialization/kotlinx-serialization-json/kotlinx.serialization.json/-json-builder/ignore-unknown-keys.html
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        chirpLogger.debug(message)
                    }
                }
                // ALL은 헤더와 본문까지 기록하므로 운영 환경에서는 로그 레벨 조정과 민감 헤더 마스킹이 필요합니다.
                // https://ktor.io/docs/client-logging.html
                level = LogLevel.ALL
            }
            install(WebSockets) {
                pingIntervalMillis = 20_000L
            }
            // defaultRequest에 둔 값은 이 클라이언트가 보내는 모든 요청에 공통으로 적용됩니다.
            // https://ktor.io/docs/client-default-request.html
            defaultRequest {
                header("x-api-key", BuildKonfig.API_KEY)
                contentType(ContentType.Application.Json)
            }
        }
    }
}
