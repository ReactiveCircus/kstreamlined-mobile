import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        compose()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.molecule.runtime)
            api(libs.androidx.compose.runtimeAnnotation)
        }
    }
}
