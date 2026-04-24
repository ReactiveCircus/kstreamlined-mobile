import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        compose()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:capsule:runtime"))
            api(libs.kotlinx.coroutines.test)
        }
    }
}
