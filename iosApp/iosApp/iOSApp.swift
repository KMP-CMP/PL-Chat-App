import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Kotlin top-level 함수는 "파일명 + Kt" 클래스의 정적 메서드로 노출된다.
        // initKoin은 이름이 init으로 시작해 ObjC 이니셜라이저 규칙과 충돌하므로 do 접두사가 붙는다.
        // https://kotlinlang.org/docs/native-objc-interop.html
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}