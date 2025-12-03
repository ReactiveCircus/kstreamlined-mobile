import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.scheduledwork") {
        hilt()
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":kmp:feed-sync:common"))
            implementation(project(":kmp:settings-datasource"))

            implementation(libs.androidx.work.runtime)
            implementation(libs.androidx.tracing)
            implementation(libs.hilt.android)

            testImplementation(project(":kmp:datastore-testing"))
            testImplementation(libs.kotlin.test.junit)
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
