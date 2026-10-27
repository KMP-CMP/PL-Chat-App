package com.freddie.core.data.di

import com.freddie.core.data.auth.KtorAuthService
import com.freddie.core.data.logging.KermitLogger
import com.freddie.core.data.networking.HttpClientFactory
import com.freddie.core.domain.auth.AuthService
import com.freddie.core.domain.logging.ChirpLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * expect/actual로 공통 DI 진입점을 유지하면서 플랫폼별 HttpClientEngine 등록을 분리한다.
 * https://kotlinlang.org/docs/multiplatform-expect-actual.html
 */
expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<ChirpLogger> { KermitLogger }
    // single은 최초 요청 때 생성한 인스턴스를 재사용한다. get()은 각 인자의 기대 타입으로
    // ChirpLogger와 HttpClientEngine을 찾아 공통 설정을 적용한 HttpClient를 구성한다.
    single {
        HttpClientFactory(get()).create(get())
    }
    // singleOf는 생성자 의존성을 자동으로 해결하고, bind는 같은 인스턴스를 AuthService로도 조회하게 한다.
    // https://insert-koin.io/docs/reference/koin-core/dsl/
    singleOf(::KtorAuthService) bind AuthService::class
}
