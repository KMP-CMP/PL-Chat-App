package com.freddie.chirp.di

import com.freddie.auth.presentation.di.authPresentationModule
import com.freddie.core.data.di.coreDataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * 공통 initKoin + KoinAppDeclaration 파라미터: KMP 관용구.
 * 모듈 등록은 commonMain에서 한 번만 하고, 플랫폼 전용 설정(Android의 androidContext 등)은
 * 각 진입점이 람다로 주입한다. 추가 설정이 없는 iOS/Desktop은 인자 없이 호출한다.
 * https://insert-koin.io/docs/reference/koin-mp/kmp
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        // module 선언만으로는 주입할 수 없으며, modules에 로드해야 정의를 찾을 수 있다.
        // RegisterViewModel의 AuthService를 해결하려면 구현체를 등록한 coreDataModule도 필요하다.
        modules(
            authPresentationModule,
            coreDataModule
        )
    }
}
