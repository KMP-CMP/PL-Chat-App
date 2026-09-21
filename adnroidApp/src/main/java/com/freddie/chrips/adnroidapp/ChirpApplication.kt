package com.freddie.chrips.adnroidapp

import android.app.Application
import com.freddie.chirp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ChirpApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            // androidContext: Application Context를 컨테이너에 등록하는 Android 전용 확장.
            // 이후 모듈들이 Context가 필요한 의존성(DataStore, Room 등)을 주입받는 통로가 된다.
            androidContext(this@ChirpApplication)
            androidLogger()
        }
    }
}
