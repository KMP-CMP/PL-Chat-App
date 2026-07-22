package com.freddie.core.data.logging

import co.touchlab.kermit.Logger
import com.freddie.core.domain.logging.ChirpLogger

// Kermit은 commonMain의 로그를 Android Logcat, iOS OSLog 같은 플랫폼별 출력으로 전달합니다.
// https://kermit.touchlab.co/docs/
object KermitLogger: ChirpLogger {
    override fun debug(message: String) {
        Logger.d(message)
    }

    override fun info(message: String) {
        Logger.i(message)
    }

    override fun warn(message: String) {
        Logger.w(message)
    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }
}
