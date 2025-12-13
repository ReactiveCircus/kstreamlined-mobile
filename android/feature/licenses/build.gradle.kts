plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.licenses") {
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":kmp:presentation:common"))
            implementation(project(":kmp:app-info"))
            implementation(libs.licentia.runtime)

            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
