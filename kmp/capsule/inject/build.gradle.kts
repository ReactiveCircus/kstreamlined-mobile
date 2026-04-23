import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        compose()
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:capsule:runtime"))
            api(libs.metro.runtime)
            api(libs.androidx.compose.runtime)
            implementation(libs.androidx.compose.runtime.retain)

            testImplementation(libs.kotlinx.coroutines.test)
        }
    }
}
