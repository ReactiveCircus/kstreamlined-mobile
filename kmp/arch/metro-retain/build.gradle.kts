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
            api(libs.metro.runtime)
            api(libs.androidx.compose.runtime)
            implementation(libs.androidx.compose.runtime.retain)
        }
    }
}
