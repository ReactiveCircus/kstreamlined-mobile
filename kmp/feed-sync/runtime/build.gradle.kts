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
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:feed-sync:common"))
            implementation(project(":kmp:remote:common"))
            implementation(project(":kmp:database"))
            implementation(project(":kmp:network-monitor:common"))
            implementation(libs.kermit)
            implementation(libs.ksoup)

            testImplementation(project(":kmp:remote:testing"))
            testImplementation(project(":kmp:database-testing"))
            testImplementation(project(":kmp:network-monitor:testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
