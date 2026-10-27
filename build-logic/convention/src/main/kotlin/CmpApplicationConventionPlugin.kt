import com.freddie.chirp.convention.configureAndroidLibraryTarget
import com.freddie.chirp.convention.configureIosTargets
import com.freddie.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                // Serialization 플러그인은 적용된 모듈의 @Serializable 타입에 직렬화 코드를 생성한다.
                // route를 정의하는 feature 모듈에도 별도로 적용되어 있어야 한다.
                // https://kotlinlang.org/docs/serialization.html
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            configureAndroidLibraryTarget()
            configureIosTargets()

            dependencies {
                "androidMainImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}
