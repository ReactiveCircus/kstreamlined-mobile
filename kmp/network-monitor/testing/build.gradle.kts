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
            api(project(":kmp:network-monitor:common"))
            api(libs.turbine)

            testImplementation(libs.kotlinx.coroutines.test)
        }
    }
}
