import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("kstreamlined")
}

kstreamlined {
    androidBenchmark(
        namespace = "io.github.reactivecircus.kstreamlined.android.benchmark",
        targetProjectPath = ":app",
        environment = ProductFlavors.Dev,
        targetAppIdKey = "targetAppId",
        minSdk = 28,
    ) {
        @Suppress("UnstableApiUsage")
        managedVirtualDevice("pixel10Api37") {
            device = "Pixel 10"
            apiLevel = 37
            systemImageSource = "google_apis"
            pageAlignment = ManagedVirtualDevice.PageAlignment.FORCE_16KB_PAGES
        }

        produceBaselineProfile("pixel10Api37")

        dependencies {
            implementation(libs.kermit)
        }
    }
}
