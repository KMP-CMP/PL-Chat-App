package com.freddie.auth.presentation.di

import com.freddie.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    // viewModelOf(::RegisterViewModel): 생성자 참조 DSL. 나중에 생성자 파라미터가 생겨도
    // Koin이 컨테이너에서 타입을 찾아 자동 주입하므로 등록 코드는 그대로 유지된다.
    // https://insert-koin.io/docs/reference/koin-core/dsl-update
    viewModelOf(::RegisterViewModel)
}
