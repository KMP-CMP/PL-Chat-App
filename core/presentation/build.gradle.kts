plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    jvm()

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
                implementation(libs.jetbrains.compose.components.resources)
                // Adaptive API를 commonMain에서 사용해 플랫폼별 창 크기 판별 로직이 중복되지 않도록 합니다.
                // Android와 iOS가 같은 breakpoint를 공유하므로 반응형 UI 분기를 일관되게 유지할 수 있습니다.
                // https://kotlinlang.org/docs/multiplatform/compose-adaptive-layouts.html
                implementation(libs.material3.adaptive)
                implementation(libs.jetbrains.lifecycle.compose)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}
