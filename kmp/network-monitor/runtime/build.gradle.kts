import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            ios()
            android("io.github.reactivecircus.kstreamlined.kmp.networkmonitor.runtime")
        }
        metro()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:network-monitor:common"))
        }
    }
}
