import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:remote:common"))
            implementation(libs.kotlinx.coroutines.core)

            testImplementation(libs.kotlinx.coroutines.test)
        }
    }
}
