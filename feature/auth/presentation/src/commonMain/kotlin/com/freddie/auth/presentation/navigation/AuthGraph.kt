package com.freddie.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.freddie.auth.presentation.email_varification.EmailVerificationRoot
import com.freddie.auth.presentation.register.RegisterRoot
import com.freddie.auth.presentation.register_success.RegisterSuccessRoot
import com.freddie.auth.presentation.register_success.RegisterSuccessScreen

/**
 * NavGraphBuilder 확장 함수로 인증 흐름을 중첩 그래프로 묶어 상위 NavHost에서 조립한다.
 * Graph는 흐름의 진입점이며, 실제 첫 화면은 내부 startDestination으로 결정된다.
 * https://developer.android.com/guide/navigation/design
 */
fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoutes.Graph>(
        startDestination = AuthGraphRoutes.Login
    ) {
        composable<AuthGraphRoutes.Login> {
            RegisterRoot(
                onRegisterSuccess = { email ->
                    // route 객체의 프로퍼티가 이동 인자가 되어 문자열 경로를 직접 조합할 필요가 없다.
                    // 수신 화면에서는 NavBackStackEntry.toRoute<AuthGraphRoutes.RegisterSuccess>()로 읽는다.
                    navController.navigate(AuthGraphRoutes.RegisterSuccess(email))
                }
            )
        }

        composable<AuthGraphRoutes.RegisterSuccess> {
            RegisterSuccessRoot()
        }

        composable<AuthGraphRoutes.EmailVerification>(
            // 두 URI 패턴의 {token}은 같은 목적지 인자에 연결된다. 인증 ViewModel은 SavedStateHandle에서 token을 읽는다.
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = "https://chirp.pl-coding.com/api/auth/verify?token={token}"
                },
                navDeepLink {
                    this.uriPattern = "chirp://chirp.pl-coding.com/api/auth/verify?token={token}"
                },
            )
        ) {
            EmailVerificationRoot()
        }
    }
}
