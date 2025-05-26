import com.android.build.api.dsl.androidLibrary

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

    @Suppress("UnstableApiUsage")
    androidLibrary {
        namespace = "io.github.reactivecircus.kmp.networkmonitor.runtime"
    }
}
