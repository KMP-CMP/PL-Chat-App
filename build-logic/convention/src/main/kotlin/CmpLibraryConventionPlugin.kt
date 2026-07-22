import com.freddie.chirp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.freddie.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material-icons-extended").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material-icons-core").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())

                // commonMain에는 @Preview 선언 API를 제공하고, Android 렌더러는 런타임 classpath에만 둡니다.
                // Android-KMP의 단일 variant에서도 tooling을 컴파일·배포 API에 노출하지 않고 Preview를 실행할 수 있습니다.
                // https://developer.android.com/kotlin/multiplatform/plugin#add-compose-preview-dependencies
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())

                "androidRuntimeClasspath"(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}
