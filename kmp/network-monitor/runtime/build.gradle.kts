import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            ios()
            android("io.github.reactivecircus.kstreamlined.kmp.networkmonitor.runtime")
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:network-monitor:common"))
        }
    }
}
