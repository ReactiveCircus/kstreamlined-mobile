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
            // TODO decouple from metro-retain / invert dependency
            implementation(project(":kmp:arch:metro-retain"))
            api(libs.molecule.runtime)
            api(libs.androidx.compose.runtime.retain)
        }
    }
}
