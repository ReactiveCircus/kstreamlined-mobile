import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined.kmp.jvm-and-ios")
    id("kstreamlined.kmp.test")
    id("kstreamlined.compose")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:presentation:common"))
        implementation(project(":kmp:feed-datasource"))

        testImplementation(project(":kmp:remote:testing"))
        testImplementation(project(":kmp:database-testing"))
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
    }
}
