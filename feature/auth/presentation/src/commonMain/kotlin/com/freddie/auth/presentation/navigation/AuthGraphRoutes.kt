package com.freddie.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthGraphRoutes {
    // 인자가 없는 route는 object로, 인자가 있는 route는 프로퍼티를 가진 class로 정의한다.
    // 각 구체 route의 @Serializable이 Navigation의 타입 기반 경로·인자 처리를 지원한다.
    // https://developer.android.com/guide/navigation/design
    @Serializable
    data object Graph: AuthGraphRoutes

    @Serializable
    data object Login: AuthGraphRoutes

    @Serializable
    data object Register: AuthGraphRoutes

    @Serializable
    data class RegisterSuccess(val email: String): AuthGraphRoutes

    @Serializable
    data object ForgotPassword: AuthGraphRoutes

    @Serializable
    data object ResetPassword: AuthGraphRoutes

    @Serializable
    data class EmailVerification(val token: String): AuthGraphRoutes
}
