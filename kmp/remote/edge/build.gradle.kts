import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined.kmp.jvm-and-ios")
    id("kstreamlined.kmp.test")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:remote:common"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kermit)

        testImplementation(libs.kotlinx.coroutines.test)
    }
}
