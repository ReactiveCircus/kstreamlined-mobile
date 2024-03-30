plugins {
    id("kstreamlined.kmp.android-and-ios")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:network-monitor:common"))
            }
        }
    }
}

android {
    namespace = "io.github.reactivecircus.kmp.networkmonitor"
}
