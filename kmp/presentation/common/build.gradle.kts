import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
    id("kstreamlined.compose")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.kotlinx.coroutines.core)
        api(libs.molecule.runtime)
        api(libs.androidx.compose.runtimeAnnotation)
    }
}
