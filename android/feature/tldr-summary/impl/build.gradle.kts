kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl") {
        routeBinding()
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":core:ui:pattern"))
            implementation(project(":feature:content-viewer:api"))
            implementation(project(":feature:tldr-summary:api"))
            implementation(project(":kmp:presentation:tldr-summary"))
        }
    }
}
