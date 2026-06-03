kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl") {
        routeBinding()
        screenshotTests()

        dependencies {
            implementation(project(":feature:feed-selection:api"))
            implementation(project(":core:route-metadata"))
            implementation(project(":kmp:presentation:feed-selection"))
            implementation(project(":kmp:feed-model"))
        }
    }
}
