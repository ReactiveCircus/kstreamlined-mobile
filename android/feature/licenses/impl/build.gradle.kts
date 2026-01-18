plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.licenses.impl") {
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":feature:licenses:api"))
            implementation(project(":core:navigation"))
            implementation(project(":kmp:presentation:common"))
            implementation(libs.licentia.runtime)

            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
