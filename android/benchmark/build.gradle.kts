import com.android.build.api.dsl.ManagedVirtualDevice
import io.github.reactivecircus.kstreamlined.gradle.minorApiLevel

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
            minorApiLevel = 2
            systemImageSource = "google_apis"
            pageAlignment = ManagedVirtualDevice.PageAlignment.FORCE_16KB_PAGES
        }

        produceBaselineProfile("pixel10Api37")

        dependencies {
            implementation(libs.kermit)
        }
    }
}
