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
            api(libs.molecule.runtime)
            api(libs.androidx.compose.runtime.retain)
        }
    }
}
