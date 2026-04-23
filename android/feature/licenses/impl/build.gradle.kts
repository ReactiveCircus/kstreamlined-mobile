kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.licenses.impl") {
        routeBinding()
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":feature:licenses:api"))
            implementation(libs.licentia.runtime)

            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
