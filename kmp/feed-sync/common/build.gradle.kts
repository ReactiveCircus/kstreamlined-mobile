import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(libs.kotlinx.coroutines.core)
        }
    }
}
